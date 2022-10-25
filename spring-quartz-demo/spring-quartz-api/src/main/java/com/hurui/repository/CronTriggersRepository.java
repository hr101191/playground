package com.hurui.repository;

import com.hurui.entity.CronTriggers;
import com.hurui.entity.TriggersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CronTriggersRepository extends JpaRepository<CronTriggers, TriggersId>, JpaSpecificationExecutor<CronTriggers> {
}
