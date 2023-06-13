package com.bsep.admin.repository;

import com.bsep.admin.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CustomMongoRepository {
    public Page<Message> findFilteredMessages(UUID deviceId, String type, String text, Float valueFrom, Float valueTo, LocalDateTime timestampFrom, LocalDateTime timestampTo, PageRequest pageRequest);

    public void keep100MostRecentMessages();
}
