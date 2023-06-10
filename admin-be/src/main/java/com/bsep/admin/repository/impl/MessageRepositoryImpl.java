package com.bsep.admin.repository.impl;

import com.bsep.admin.model.Message;
import com.bsep.admin.repository.CustomMongoRepository;
import com.bsep.admin.repository.MessageRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepositoryImpl implements CustomMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<Message> findFilteredMessages(UUID deviceId, String type, String text, Integer valueFrom, Integer valueTo, LocalDateTime timestampFrom, LocalDateTime timestampTo, PageRequest pageRequest) {
        Query query = new Query();

        query.addCriteria(Criteria.where("deviceId").is(deviceId));

        if (!StringUtils.isEmpty(type)) {
            query.addCriteria(Criteria.where("type").is(type));
        }

        if (!StringUtils.isEmpty(text)) {
            query.addCriteria(Criteria.where("text").regex(text, "i")); // 'i' is for case insensitive matching
        }

        if (valueFrom != null) {
            query.addCriteria(Criteria.where("value").gte(valueFrom).orOperator(Criteria.where("value").lte(valueTo)));
        }

        if (timestampFrom != null) {
            query.addCriteria(Criteria.where("timestamp").gte(timestampFrom).orOperator(Criteria.where("timestamp").lte(timestampTo)));
        }

        query.with(pageRequest);

        List<Message> messages = mongoTemplate.find(query, Message.class);

        long count = mongoTemplate.count(query, Message.class);

        return PageableExecutionUtils.getPage(messages, pageRequest, () -> mongoTemplate.count((Query.of(query).limit(-1).skip(-1)), Message.class));
    }

}
