package com.bsep.admin.repository;

import org.springframework.transaction.annotation.Transactional;

public interface CustomLogRepository {
    public void keep100MostRecentMessages();

}
