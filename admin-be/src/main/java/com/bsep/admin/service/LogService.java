package com.bsep.admin.service;

import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogType;
import com.bsep.admin.myHouse.dto.LogSearchResultDto;
import com.bsep.admin.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.data.domain.Sort;

import java.util.*;

@Service
public class LogService {

    @Autowired
    private LogRulesService logRulesService;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

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
        logRepository.keep100MostRecentMessages();
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

    public LogSearchResultDto searchLogs(String actionQuery, String detailsQuery, String ipAddressQuery,
                                String logType, List<String> usernames, boolean regexEnabled, int page, int amount) {
        Query query = new Query();

        if (regexEnabled) {
            if (actionQuery != null) {
                query.addCriteria(Criteria.where("action").regex(actionQuery));
            }
            if (detailsQuery != null) {
                query.addCriteria(Criteria.where("details").regex(detailsQuery));
            }
            if (ipAddressQuery != null) {
                query.addCriteria(Criteria.where("ipAddress").regex(ipAddressQuery));
            }
        } else {
            if (actionQuery != null) {
                query.addCriteria(Criteria.where("action").regex(".*" + actionQuery + ".*", "i"));
            }
            if (detailsQuery != null) {
                query.addCriteria(Criteria.where("details").regex(".*" + detailsQuery + ".*", "i"));
            }
            if (ipAddressQuery != null) {
                query.addCriteria(Criteria.where("ipAddress").regex(".*" + ipAddressQuery + ".*", "i"));
            }
        }

        if (logType != null && logType.length() > 0) {
            query.addCriteria(Criteria.where("type").is(logType));
        }
        if (usernames != null && !usernames.isEmpty()) {
            query.addCriteria(Criteria.where("usernames").all(usernames));
        }

        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));
        long totalItems = mongoTemplate.count(query, Log.class);

        query.skip((page - 1) * amount).limit(amount);

        List<Log> logs = mongoTemplate.find(query, Log.class);
        return new LogSearchResultDto(logs, totalItems);
    }

}
