package com.hurui.repository;

import com.hurui.entity.SimpropTriggers;
import com.hurui.entity.TriggersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpropTriggersRepository extends JpaRepository<SimpropTriggers, TriggersId>, JpaSpecificationExecutor<SimpropTriggers> {
}
