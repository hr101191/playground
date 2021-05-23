package com.hurui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;

import com.hurui.model.api.TransactionReq;
import com.hurui.model.api.TransactionResp;
import com.hurui.model.dao.Transaction;
import com.hurui.utils.TransactionTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

	private final R2dbcEntityTemplate r2dbcEntityTemplate;
	
	@Override
	public Mono<TransactionResp> getTransactionsByEmail(TransactionReq request) {
		List<Criteria> transferSearchCriterias = new ArrayList<>();
		transferSearchCriterias.add(Criteria.where("EMAIL_FROM").is(request.getEmail()));
		transferSearchCriterias.add(Criteria.where("TRANSFER_TYPE").is(TransactionTypeEnum.transfer));
		List<Criteria> receiveSearchCriterias = new ArrayList<>();
		receiveSearchCriterias.add(Criteria.where("EMAIL_TO").is(request.getEmail()));
		receiveSearchCriterias.add(Criteria.where("TRANSFER_TYPE").is(TransactionTypeEnum.receive));
		log.info("Retrieving transactions of account: {}", request.getEmail());
		return Flux.merge(r2dbcEntityTemplate.select(Query.query(CriteriaDefinition.from(transferSearchCriterias)), Transaction.class), 
				r2dbcEntityTemplate.select(Query.query(CriteriaDefinition.from(receiveSearchCriterias)), Transaction.class))
			.collectList()
			.switchIfEmpty(Mono.just(new ArrayList<>()))
			.flatMap(transactions-> {
				log.info("Retriedtransactions of account: {} | Transactions: {}", request.getEmail(), transactions);
				return Mono.just(TransactionResp.builder()
						.success(Boolean.TRUE)
						.transactions(transactions)
						.build());
			})
			.doOnError(onError -> {
				log.error("Failed to retrieve account transactions. Stacktrace: ", onError);
			});
	}
}
