package com.hurui.repository;

import com.hurui.entity.SchedulerState;
import com.hurui.entity.SchedulerStateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerStateRepository extends JpaRepository<SchedulerState, SchedulerStateId>, JpaSpecificationExecutor<SchedulerState> {
}
