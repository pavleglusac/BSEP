package com.bsep.admin.repository;

import com.bsep.admin.myHouse.dto.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleRepository extends JpaRepository<Rule, UUID> {

    Optional<Rule> findByName(String name);
}
