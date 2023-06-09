package com.bsep.admin.repository;

import com.bsep.admin.model.LogAlarm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogAlarmRepository extends MongoRepository<LogAlarm, UUID> {
}
