package com.bsep.admin.repository;

import com.bsep.admin.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface LogRepository extends MongoRepository<Log, UUID>, CustomLogRepository {

}
