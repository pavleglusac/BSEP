package com.bsep.admin.service;

import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogType;
import com.bsep.admin.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LogService {

    @Autowired
    private LogRulesService logRulesService;
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logAction(LogType type, String action, String details, String... usernames) {
        Log log = new Log();
        log.setId(UUID.randomUUID());
        log.setTimestamp(new Date());
        log.setType(type);
        log.setAction(action);
        log.setDetails(details);
        log.setIpAddress(getClientIP());
        List<String> usernamesList = new ArrayList<>(getClientUsername().map(List::of).orElse(List.of()));
        usernamesList.addAll(List.of(usernames));
        log.setUsernames(usernamesList);
        log.setUsernames(List.of(usernames));
        log.setRead(false);
        logRepository.save(log);
        logRulesService.addMessage(log);
    }

    private String getClientIP() {
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "";
        }
    }

    private Optional<String> getClientUsername() {
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            return Optional.of(request.getUserPrincipal().getName());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

}
