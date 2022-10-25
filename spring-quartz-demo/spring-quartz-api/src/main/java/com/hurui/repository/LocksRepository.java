package com.hurui.repository;

import com.hurui.entity.Locks;
import com.hurui.entity.LocksId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocksRepository extends JpaRepository<Locks, LocksId>, JpaSpecificationExecutor<Locks> {
}
