package com.bsep.admin.repository;

import com.bsep.admin.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface LogRepository extends MongoRepository<Log, UUID>, CustomLogRepository {
    @Query("{$or: [ { 'actionQuery': { $regex: ?0, $options: 'i' } }, " +
            "{ 'detailsQuery': { $regex: ?1, $options: 'i' } }, " +
            "{ 'ipAddressQuery': { $regex: ?2, $options: 'i' } } ] }")
    List<Log> search(String actionQuery, String detailsQuery, String ipAddressQuery);
}
