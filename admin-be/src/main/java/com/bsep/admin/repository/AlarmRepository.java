package com.bsep.admin.repository;

import com.bsep.admin.model.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlarmRepository extends MongoRepository<Alarm, UUID> {

    List<Alarm> findAllByDeviceId(UUID deviceId);

    List<Alarm> findAllByDeviceIdIn(List<UUID> deviceIds);

    Page<Alarm> findAllByDeviceIdIn(List<String> deviceIds, Pageable pageable);

}
