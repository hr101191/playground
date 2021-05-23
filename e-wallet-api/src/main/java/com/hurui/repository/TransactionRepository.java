package com.hurui.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hurui.model.dao.Transaction;

@Repository
@Transactional
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {

}
