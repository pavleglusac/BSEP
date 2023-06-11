package com.bsep.admin.myHouse;

import com.bsep.admin.model.Message;
import com.bsep.admin.myHouse.dto.Rule;
import com.bsep.admin.repository.RuleRepository;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RulesService {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private RuleRepository ruleRepository;

    private List<String> DRL = new ArrayList<>();

    private final String DRL_header = """
            import com.bsep.admin.model.*;
            import java.util.List;
            import java.util.ArrayList;
            import java.util.Date;
            import java.util.concurrent.TimeUnit;
            import com.bsep.admin.myHouse.AlarmService;
            import com.bsep.admin.service.LogAlarmService;
            global AlarmService alarmService;
            global LogAlarmService logAlarmService;
    """;

    private String ruleStartMarker = "// << ";
    private String ruleEndMarker = "// >> ";

    private KieSession kieSession;

    private String msgRuleTemplate = "Message($mid: deviceId, read == false)";
    private String accumulateRuleTemplate = "$l: List() from collect( \n" +
                                                "   Message($m: this, read==false, deviceId == $mid {TEMPLATE_TEXT_REGEX}{TEMPLATE_OPERATOR_AND_VALUE}{TEMPLATE_DEVICE_TYPE})" +
                                            "   {TEMPLATE_WINDOW}\n" +
                                            " )\n" +
                                            " eval($l.size() {TEMPLATE_OPERATOR_AND_NUM})";

    // add thread safe queue of messages
    ArrayDeque<Message> queue = new ArrayDeque<>();



    @Scheduled(fixedRate = 2000)
    public void fireAllRules() {
        System.out.println("Firing all rules! Total messages: " + queue.size());
        StringBuilder rules = new StringBuilder();
        rules.append(DRL_header);
        DRL.forEach(line -> rules.append(line).append("\n"));


        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();

        kfs.write("src/main/resources/rules.drl", rules.toString());
        kieServices.newKieBuilder(kfs).buildAll();

        ReleaseId releaseId = kieServices.getRepository().getDefaultReleaseId();
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        
        // The new session now contains the updated rules
        kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("alarmService", alarmService);

        for (Message message : queue) {
//            System.out.println("Inserting message: " + message);
            kieSession.insert(message);
        }

        kieSession.fireAllRules();
    }


    public void addMessage(Message message) {
        if (queue.size() > 1000) {
            queue.poll();
        }
        queue.add(message);
//        System.out.println("\n++++++++++++ Added msg, totasl len " + queue.size() + " ++++++++++++\n");
    }


    public void deleteRule(String ruleName) {
        // find rule in list of rules and delete it
        Rule rule = ruleRepository.findByName(ruleName).orElseThrow(() -> new RuntimeException("Rule not found!"));
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

    public void addRule(Rule rule) {
        List<String> res = buildRuleTemplate(rule);
        rule.setId(UUID.randomUUID());

        // append rule to DRL
        DRL.addAll(res);
        // print all rules
        DRL.forEach(System.out::println);

        ruleRepository.save(rule);
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    private List<String> buildRuleTemplate(Rule rule) {
        String templateTextRegex = null;
        String templateOperatorAndValue = null;
        String templateWindow = null;
        String templateOperatorAndNum = null;
        String templateDeviceType = null;

        if (rule.getTextRegex() != null && !rule.getTextRegex().isEmpty()) {
            templateTextRegex = ", text matches \"" + rule.getTextRegex() + "\"";
        }

        if (rule.getOperatorValue() != null && !rule.getOperatorValue().isEmpty() && rule.getValue() != null) {
            templateOperatorAndValue = ", " + rule.getOperatorValue() + " " + rule.getValue();
        }

        if (rule.getWindow() != null && !rule.getWindow().isEmpty()) {
            templateWindow = ", over window:time(" + rule.getWindow() + ")";
        }

        if (rule.getOperatorNum() != null && !rule.getOperatorNum().isEmpty() && rule.getNum() != null) {
            templateOperatorAndNum = rule.getOperatorNum() + " " + rule.getNum();
        }

        if (rule.getDeviceType() != null) {
            templateDeviceType = ", deviceType == DeviceType." + rule.getDeviceType();
        }

        // add rule to list of rules
        List<String> res = new ArrayList<>();
        res.add(ruleStartMarker + rule.getName());
        res.add("rule \"" + rule.getName() + "\"");
        res.add("when");
        res.add(msgRuleTemplate);
        res.add(accumulateRuleTemplate
                .replace("{TEMPLATE_DEVICE_TYPE}", Optional.ofNullable(templateDeviceType).map(Objects::toString).orElse(""))
                .replace("{TEMPLATE_TEXT_REGEX}", Optional.ofNullable(templateTextRegex).orElse(""))
                .replace("{TEMPLATE_OPERATOR_AND_VALUE}", Optional.ofNullable(templateOperatorAndValue).orElse(""))
                .replace("{TEMPLATE_WINDOW}", Optional.ofNullable(templateWindow).orElse(""))
                .replace("{TEMPLATE_OPERATOR_AND_NUM}", Optional.ofNullable(templateOperatorAndNum).orElse("")));
        res.add("then");
        String then = String.format("alarmService.createAlarm(\"%s\", \"%s\", $mid);", rule.getName(), rule.getAlarmText()) +
            """

            for(Object msg : $l) {
                modify((Message)msg) {
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
