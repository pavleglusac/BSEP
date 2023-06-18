package com.bsep.admin.repository;

import com.bsep.admin.model.Alarm;
import com.bsep.admin.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CustomMongoRepository {
    public Page<Message> findFilteredMessages(UUID deviceId, String type, String text, Float valueFrom, Float valueTo, LocalDateTime timestampFrom, LocalDateTime timestampTo, PageRequest pageRequest);

    public Page<Message> findFilteredMessages(List<UUID> deviceIds, String type, PageRequest pageRequest);

    public void keep100MostRecentMessages();

    Page<Alarm> findAllByDeviceIdIn(List<String> deviceIds, Pageable pageable);
}
