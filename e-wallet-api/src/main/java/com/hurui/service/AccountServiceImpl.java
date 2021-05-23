package com.hurui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;

import com.hurui.model.api.AccountRegistrationReq;
import com.hurui.model.api.AccountRegistrationResp;
import com.hurui.model.dao.Account;
import com.hurui.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	
	private final R2dbcEntityTemplate r2dbcEntityTemplate;
	
	@Override
	public Mono<AccountRegistrationResp> createAccount(AccountRegistrationReq request) {
		List<Criteria> searchCriterias = new ArrayList<>();
		searchCriterias.add(Criteria.where("EMAIL").is(request.getEmail()));
		return r2dbcEntityTemplate.count(Query.query(CriteriaDefinition.from(searchCriterias)), Account.class)
			.flatMap(rowCount -> {
				if (rowCount == 0) {
					log.info("Creating new account: {}", request.getEmail());
					return accountRepository.save(Account.builder()
							.email(request.getEmail())
							.balance(10000.00) //default logic - add 10,000 to created account 
							.build())							
							.map(accountCreated -> AccountRegistrationResp.builder()
									.balance(accountCreated.getBalance())
									.success(Boolean.TRUE)
									.build());
				} else {
					log.warn("Account |{}| already exists. Account is not created!", request.getEmail());
					return Mono.just(AccountRegistrationResp.builder()
							.success(Boolean.FALSE)
							.balance(0.0)
							.build());
				}
			})
			.doOnError(onError -> {
				log.error("Failed to create account. Stacktrace: ", onError);
			});
	}
}
