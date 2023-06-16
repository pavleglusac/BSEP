package com.bsep.admin.repository.impl;

import com.bsep.admin.model.Alarm;
import com.bsep.admin.model.Message;
import com.bsep.admin.repository.CustomMongoRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class AlarmRepositoryImpl implements CustomMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Alarm> findAllByDeviceIdIn(List<String> deviceIds, Pageable pageable) {

        Query query = new Query(Criteria.where("deviceId").in(deviceIds));

        query.with(pageable);

        List<Alarm> alarms = mongoTemplate.find(query, Alarm.class);

        long count = mongoTemplate.count(query, Alarm.class);

        return PageableExecutionUtils.getPage(alarms, pageable, () -> mongoTemplate.count((Query.of(query).limit(-1).skip(-1)), Alarm.class));
    }

    public Page<Message> findFilteredMessages(UUID deviceId, String type, String text, Float valueFrom, Float valueTo, LocalDateTime timestampFrom, LocalDateTime timestampTo, PageRequest pageRequest) {
        return null;
    }

    public Page<Message> findFilteredMessages(List<UUID> deviceIds, String type, PageRequest pageRequest) {
        return null;
    }

    public void keep100MostRecentMessages() {
        // create a query that sorts by timestamp descending and limits to 100
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "timestamp")).limit(100);
        // find the 100 most recent messages
        List<Message> messages = mongoTemplate.find(query, Message.class);
        // delete all messages
        mongoTemplate.remove(new Query(), Message.class);
        // save the 100 most recent messages
        mongoTemplate.insert(messages, Message.class);
    }


}
