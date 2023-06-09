package com.bsep.admin.service;

import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogType;
import com.bsep.admin.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logAction(LogType type, String action, String details, String... usernames) {
        Log log = new Log();
        log.setTimestamp(LocalDateTime.now());
        log.setType(type);
        log.setAction(action);
        log.setDetails(details);
        log.setIpAddress(getClientIP());
        List<String> usernamesList = new ArrayList<>(getClientUsername().map(List::of).orElse(List.of()));
        usernamesList.addAll(List.of(usernames));
        log.setUsernames(usernamesList);
        log.setUsernames(List.of(usernames));
        logRepository.save(log);
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

}
