package com.bsep.admin.myHouse;

import com.bsep.admin.model.Message;
import com.bsep.admin.myHouse.dto.RuleDto;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class RulesService {

    private List<String> DRL = new ArrayList<>();

    private final String DRL_header = """
            import com.bsep.admin.model.*;
            import java.util.List;
            import java.util.ArrayList;
            import java.util.Date;
            import java.util.concurrent.TimeUnit;
    """;

    private String ruleStartMarker = "// << ";
    private String ruleEndMarker = "// >> ";

    private KieSession kieSession;

    private String msgRuleTemplate = "Message($mid: id{TEMPLATE_DEVICE_TYPE})";
    private String accumulateRuleTemplate = "$num: Number() from accumulate( \n" +
                                            "   $msg: Message( id == $mid {TEMPLATE_TEXT_REGEX}{TEMPLATE_OPERATOR_AND_VALUE})" +
                                            "   {TEMPLATE_WINDOW}, \n" +
                                            "   count() \n" +
                                            " )\n" +
                                            " eval($num.intValue() {TEMPLATE_OPERATOR_AND_NUM})";

    // add thread safe queue of messages
    ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();


    @Autowired
    private KieContainer kieContainer;

    @Scheduled(fixedRate = 2000)
    public void fireAllRules() {
        System.out.println("Firing all rules!");
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

        for (Message message : queue) {
            System.out.println("Inserting message: " + message);
            kieSession.insert(message);
        }

        kieSession.fireAllRules();
    }


    public void addMessage(Message message) {
        queue.add(message);
    }


    public void deleteRule(String ruleName) {
        // find rule in list of rules and delete it
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

    public void addRule(RuleDto ruleDto) {
        String templateTextRegex = null;
        String templateOperatorAndValue = null;
        String templateWindow = null;
        String templateOperatorAndNum = null;

        if (ruleDto.getTextRegex() != null && !ruleDto.getTextRegex().isEmpty()) {
            templateTextRegex = ", text matches \"" + ruleDto.getTextRegex() + "\"";
        }

        if (ruleDto.getOperatorValue() != null && !ruleDto.getOperatorValue().isEmpty() && ruleDto.getValue() != null) {
            templateOperatorAndValue = ", " + ruleDto.getOperatorValue() + " " + ruleDto.getValue();
        }

        if (ruleDto.getWindow() != null && !ruleDto.getWindow().isEmpty()) {
            templateWindow = ", over window:time(" + ruleDto.getWindow() + ")";
        }

        if (ruleDto.getOperatorNum() != null && !ruleDto.getOperatorNum().isEmpty() && ruleDto.getNum() != null) {
            templateOperatorAndNum = ruleDto.getOperatorNum() + " " + ruleDto.getNum();
        }

        System.out.println("OVO SE DESI - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        // add rule to list of rules
        List<String> res = new ArrayList<>();
        res.add(ruleStartMarker + ruleDto.getName());
        res.add("rule \"" + ruleDto.getName() + "\"");
        res.add("when");
        res.add(msgRuleTemplate.replace("{TEMPLATE_DEVICE_TYPE}", Optional.ofNullable(ruleDto.getDeviceType()).map(Objects::toString).orElse("")));
        res.add(accumulateRuleTemplate
                .replace("{TEMPLATE_TEXT_REGEX}", Optional.ofNullable(templateTextRegex).orElse(""))
                .replace("{TEMPLATE_OPERATOR_AND_VALUE}", Optional.ofNullable(templateOperatorAndValue).orElse(""))
                .replace("{TEMPLATE_WINDOW}", Optional.ofNullable(templateWindow).orElse(""))
                .replace("{TEMPLATE_OPERATOR_AND_NUM}", Optional.ofNullable(templateOperatorAndNum).orElse("")));
        res.add("then");
        res.add("\tSystem.out.println(\"" + ruleDto.getName() + " rule fired!\");");
        res.add("end");
        res.add(ruleEndMarker + ruleDto.getName());

        // append rule to DRL
        DRL.addAll(res);

        // print all rules
        DRL.forEach(System.out::println);


    }




}
