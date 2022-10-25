package com.hurui.repository;

import com.hurui.entity.JobDetails;
import com.hurui.entity.JobDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, JobDetailsId>, JpaSpecificationExecutor<JobDetails> {
}
