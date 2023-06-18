package com.bsep.admin.service;
import com.bsep.admin.dto.LogRuleCreationDto;
import com.bsep.admin.exception.InvalidRuleException;
import com.bsep.admin.exception.RuleNotFoundException;
import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogRule;
import com.bsep.admin.model.Message;
import com.bsep.admin.myHouse.dto.Rule;
import com.bsep.admin.repository.LogRuleRepository;
import com.bsep.admin.repository.RuleRepository;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Service

public class LogRulesService {

    @Autowired
    private LogAlarmService alarmService;

    @Autowired
    private LogRuleRepository ruleRepository;

    private List<String> DRL = new ArrayList<>();

    private final String DRL_header = """
        import com.bsep.admin.model.*;
        import java.util.List;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.concurrent.TimeUnit;
        import com.bsep.admin.service.LogAlarmService;
        import com.bsep.admin.myHouse.AlarmService;
        global LogAlarmService logAlarmService;
        global AlarmService alarmService;
""";

    private String ruleStartMarker = "// << ";
    private String ruleEndMarker = "// >> ";

    private KieSession kieSession;

    private String msgRuleTemplate = "Log(read == false)";
    private String accumulateRuleTemplate = "$l: List() from collect( \n" +
            "   Log($m: this, read==false {TEMPLATE_ACTION}{TEMPLATE_DETAILS}{TEMPLATE_IP}{TEMPLATE_TYPE}{TEMPLATE_USERNAMES})" +
            "   {TEMPLATE_WINDOW}\n" +
            " )\n" +
            " eval($l.size() {TEMPLATE_OPERATOR_AND_NUM})";

    // add thread safe queue of messages
    ConcurrentLinkedDeque<Log> queue = new ConcurrentLinkedDeque<>();



    @Scheduled(fixedRate = 2000)
    public void fireAllRules() {
        System.out.println("Firing all log rules! Total messages: " + queue.size());
//        System.out.println(queue.toString());
        StringBuilder rules = new StringBuilder();
        rules.append(DRL_header);
        DRL.forEach(line -> rules.append(line).append("\n"));


        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();

        kfs.write("src/main/resources/log_rules.drl", rules.toString());
        kieServices.newKieBuilder(kfs).buildAll();

        ReleaseId releaseId = kieServices.getRepository().getDefaultReleaseId();
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);

        KieBaseConfiguration kieBaseConfig = KieServices.Factory.get().newKieBaseConfiguration();
        // Set the event processing mode to "stream"
        kieBaseConfig.setOption(EventProcessingOption.STREAM);

        KieBase kieBase = kieContainer.newKieBase(kieBaseConfig);

        // The new session now contains the updated rules
        kieSession = kieBase.newKieSession();
        kieSession.setGlobal("logAlarmService", alarmService);

        for (Log message : queue) {
//            System.out.println("Inserting message: " + message);
            kieSession.insert(message);
        }

        kieSession.fireAllRules();
    }


    public void addMessage(Log message) {
        if (queue.size() > 100) {
            queue.poll();
        }
        queue.add(message);
//        System.out.println("\n++++++++++++ Added msg, totasl len " + queue.size() + " ++++++++++++\n");
    }


    public void deleteRule(String ruleName) {
        // find rule in list of rules and delete it
        LogRule rule = ruleRepository.findByName(ruleName).orElseThrow(() -> new RuleNotFoundException("Rule not found!"));
        ruleRepository.delete(rule);

        List<String> resultDRL = new ArrayList<>();
        boolean found = false;
        for (String line : DRL) {
            if (line.startsWith(ruleStartMarker + ruleName)) {
                found = true;
            }
            if (!found) {
                resultDRL.add(line);
            }
            if (line.contains(ruleEndMarker + ruleName)) {
                found = false;
            }
        }
        DRL = resultDRL;
    }

    public void addRule(LogRuleCreationDto dto) {
        LogRule rule = mapLogRuleDtoToLogRule(dto);
        // check if rule with same name already exists
        if (ruleRepository.findByName(rule.getName()).isPresent()) {
            throw new InvalidRuleException("Rule with same name already exists!");
        }
        List<String> res = buildRuleTemplate(rule);
        rule.setId(UUID.randomUUID());

        // append rule to DRL
        DRL.addAll(res);
        // print all rules
        DRL.forEach(System.out::println);

        ruleRepository.save(rule);
    }

    private LogRule mapLogRuleDtoToLogRule(LogRuleCreationDto dto) {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName(dto.getName());
        rule.setAlarmText(dto.getAlarmText());
        rule.setLogType(dto.getLogType());
        rule.setActionRegex(dto.getActionRegex());
        rule.setDetailsRegex(dto.getDetailsRegex());
        rule.setIpAddressRegex(dto.getIpAddressRegex());
        rule.setUsernames(dto.getUsernames());
        rule.setNum(dto.getNum());
        rule.setOperatorNum(dto.getOperatorNum());
        rule.setWindow(dto.getWindow());
        return rule;
    }

    public List<LogRule> getAllRules() {
        return ruleRepository.findAll();
    }

    private List<String> buildRuleTemplate(LogRule rule) {
        String templateActionRegex = null;
        String templateDetailsRegex = null;
        String templateIpAddressRegex = null;
        String templateUsernamesRegex = null;
        String templateType = null;
        String templateWindow = null;
        String templateOperatorAndNum = null;

        if (rule.getActionRegex() != null && !rule.getActionRegex().isEmpty()) {
            templateActionRegex = ", action matches \"" + rule.getActionRegex() + "\"";
        }

        if (rule.getDetailsRegex() != null && !rule.getDetailsRegex().isEmpty()) {
            templateDetailsRegex = ", details matches \"" + rule.getDetailsRegex() + "\"";
        }

        if (rule.getIpAddressRegex() != null && !rule.getIpAddressRegex().isEmpty()) {
            templateIpAddressRegex = ", ipAddress matches \"" + rule.getIpAddressRegex() + "\"";
        }

        if (rule.getUsernames() != null && !rule.getUsernames().isEmpty()) {
            String result = rule.getUsernames().stream()
                    .map(s -> "\"" + s + "\"")
                    .collect(Collectors.joining(","));
            templateUsernamesRegex = ", usernames.containsAll(java.util.Arrays.asList(" + result + "))";
        }

        if (rule.getLogType() != null) {
            templateType = ", type == LogType." + rule.getLogType() + "";
        }

        if (rule.getWindow() != null && !rule.getWindow().isEmpty()) {
            templateWindow = " over window:time(" + rule.getWindow() + ")";
        }

        if (rule.getOperatorNum() != null && !rule.getOperatorNum().isEmpty() && rule.getNum() != null) {
            templateOperatorAndNum = rule.getOperatorNum() + " " + rule.getNum();
        }


        // add rule to list of rules
        List<String> res = new ArrayList<>();
        res.add(ruleStartMarker + rule.getName());
        res.add("rule \"" + rule.getName() + "\"");
        res.add("when");
        res.add(msgRuleTemplate);
        res.add(accumulateRuleTemplate
                .replace("{TEMPLATE_ACTION}", Optional.ofNullable(templateActionRegex).orElse(""))
                .replace("{TEMPLATE_DETAILS}", Optional.ofNullable(templateDetailsRegex).orElse(""))
                .replace("{TEMPLATE_IP}", Optional.ofNullable(templateIpAddressRegex).orElse(""))
                .replace("{TEMPLATE_TYPE}", Optional.ofNullable(templateType).orElse(""))
                .replace("{TEMPLATE_USERNAMES}", Optional.ofNullable(templateUsernamesRegex).orElse(""))
                .replace("{TEMPLATE_WINDOW}", Optional.ofNullable(templateWindow).orElse(""))
                .replace("{TEMPLATE_OPERATOR_AND_NUM}", Optional.ofNullable(templateOperatorAndNum).orElse("")));
        res.add("then");
        String then = String.format("logAlarmService.createAlarm(\"%s\", \"%s\", $l);", rule.getName(), rule.getAlarmText()) +
                """
    
                for(Object msg : $l) {
                    modify((Log)msg) {
                        setRead(true)
                    }
                }
            """;
        res.add(then);
        res.add("end");
        res.add(ruleEndMarker + rule.getName());
        return res;
    }



}
