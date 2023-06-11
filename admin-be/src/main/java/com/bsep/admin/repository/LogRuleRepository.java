package com.bsep.admin.repository;

import com.bsep.admin.model.LogRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LogRuleRepository extends JpaRepository<LogRule, UUID> {
    Optional<LogRule> findByName(String ruleName);
}
