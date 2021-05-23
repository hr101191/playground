package com.hurui.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hurui.model.dao.Account;

@Repository
@Transactional
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

}
