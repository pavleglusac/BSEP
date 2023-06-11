package com.bsep.admin;


import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogRule;
import com.bsep.admin.model.LogType;
import com.bsep.admin.service.LogRulesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Profile("test")
public class LogRulesTest {

    @Autowired
    private LogRulesService rulesService;

    @Test
    public void testAddRule() {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        rulesService.addMessage(log1);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testActionRegex() {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        rule.setActionRegex(".*Testno.*");
        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        rulesService.addMessage(log1);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDetailsRegex() {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        rule.setDetailsRegex(".*Ovo.*");
        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        rulesService.addMessage(log1);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListOfUsernamesRule() {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        rule.setUsernames(List.of("admin", "admin2"));
        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        log1.setUsernames(List.of("admin", "admin2"));
        rulesService.addMessage(log1);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testListOfUsernamesSubsetRule() {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        rule.setUsernames(List.of("admin"));
        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        log1.setUsernames(List.of("admin", "admin2"));
        rulesService.addMessage(log1);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIpAddressRegexRule() {
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        // set regex to match 192.168.1.*
        rule.setIpAddressRegex("192\\\\.168\\\\.1\\\\..*");

        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        log1.setIpAddress("192.168.1.2");
        rulesService.addMessage(log1);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testLogTypes() {
        // create multiple logs with different types, match only one
        LogRule rule = new LogRule();
        rule.setId(UUID.randomUUID());
        rule.setName("TEST");
        rule.setNum(1);
        rule.setOperatorNum(">=");
        rule.setLogType("ERROR");

        rulesService.addRule(rule);

        Log log1 = new Log();
        log1.setId(UUID.randomUUID());
        log1.setRead(false);
        log1.setTimestamp(LocalDateTime.now());
        log1.setAction("Testno pravilo");
        log1.setDetails("Ovo su detalji testnog pravila");
        log1.setType(LogType.INFO);
        rulesService.addMessage(log1);

        Log log2 = new Log();
        log2.setId(UUID.randomUUID());
        log2.setRead(false);
        log2.setTimestamp(LocalDateTime.now());
        log2.setAction("Testno pravilo 2");
        log2.setDetails("Ovo su detalji testnog pravila 2");
        log2.setType(LogType.ERROR);
        rulesService.addMessage(log2);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
