package com.bsep.admin.repository;

import com.bsep.admin.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends MongoRepository<Message, String>, CustomMongoRepository {
    public Page<Message> findFilteredMessages(UUID deviceId, String type, String text, Integer valueFrom, Integer valueTo, LocalDateTime timestampFrom, LocalDateTime timestampTo, PageRequest pageRequest);
}
