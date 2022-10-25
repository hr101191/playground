package com.hurui.repository;

import com.hurui.entity.PausedTriggerGrps;
import com.hurui.entity.PausedTriggerGrpsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PausedTriggerGrpsRepository extends JpaRepository<PausedTriggerGrps, PausedTriggerGrpsId>, JpaSpecificationExecutor<PausedTriggerGrps> {
}
