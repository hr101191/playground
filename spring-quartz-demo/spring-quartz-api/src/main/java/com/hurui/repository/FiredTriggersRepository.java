package com.hurui.repository;

import com.hurui.entity.FiredTriggers;
import com.hurui.entity.FiredTriggersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FiredTriggersRepository extends JpaRepository<FiredTriggers, FiredTriggersId>, JpaSpecificationExecutor<FiredTriggers> {
}
