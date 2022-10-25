package com.hurui.repository;

import com.hurui.entity.BlobTriggers;
import com.hurui.entity.TriggersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BlobTriggersRepository extends JpaRepository<BlobTriggers, TriggersId>, JpaSpecificationExecutor<BlobTriggers> {
}
