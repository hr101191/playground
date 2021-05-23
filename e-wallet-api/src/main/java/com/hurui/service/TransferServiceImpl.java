package com.hurui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;

import com.hurui.model.api.TransferReq;
import com.hurui.model.api.TransferResp;
import com.hurui.model.dao.Account;
import com.hurui.model.dao.Transaction;
import com.hurui.repository.AccountRepository;
import com.hurui.repository.TransactionRepository;
import com.hurui.utils.TransactionTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransferServiceImpl implements TransferService {
	
	private final AccountRepository accountRepository;
	
	private final TransactionRepository transactionRepository;
	
	private final R2dbcEntityTemplate r2dbcEntityTemplate;
	
	@Override
	public Mono<TransferResp> transfer(TransferReq request) {
		List<Criteria> senderSearchCriterias = new ArrayList<>();
		senderSearchCriterias.add(Criteria.where("EMAIL").is(request.getEmail()));
		List<Criteria> recipientSearchCriterias = new ArrayList<>();
		recipientSearchCriterias.add(Criteria.where("EMAIL").is(request.getTransferee()));
		return Mono.zip(r2dbcEntityTemplate.selectOne(Query.query(CriteriaDefinition.from(senderSearchCriterias)), Account.class), 
				r2dbcEntityTemplate.selectOne(Query.query(CriteriaDefinition.from(recipientSearchCriterias)), Account.class))
			.flatMap(tuple -> {
				// if both sender and receiver are valid			
				log.info("Sender and receipent accounts are valid, attempting to transfer.");
				return attemptTransfer(tuple, request);				
			})
			.switchIfEmpty(Mono.just(TransferResp.builder()
								.success(Boolean.FALSE)
								.build()));	
	}
	
	private Mono<TransferResp> attemptTransfer(Tuple2<Account, Account> accounts, TransferReq request) {
		if(accounts.getT1().getBalance() < request.getAmount()) {
			// insufficient amount
			return Mono.just(TransferResp.builder()
					.success(Boolean.FALSE)
					.build());
		} else {
			Account sender = accounts.getT1();
			Account recipient = accounts.getT2();
			sender.setBalance(accounts.getT1().getBalance() - request.getAmount());
			recipient.setBalance(accounts.getT2().getBalance() + request.getAmount());
			return Mono.zip(accountRepository.save(sender), accountRepository.save(recipient))
					.doOnSuccess(onSuccess -> {
						//side effect save transaction record - mentioned in user story example that transaction record will only be saved for sender
						//logically transaction record should also be saved for recipient. Note: Always consult users and make sure they update the user story accordingly
						log.info("Sender and receipient accounts updated, saving transaction records.");
						Transaction senderTransaction = Transaction.builder()
								.from(request.getEmail())
								.to(request.getTransferee())
								.amount(request.getAmount())
								.type(TransactionTypeEnum.transfer)
								.dateTime(LocalDateTime.now())
								.build();
						Transaction recipientTransaction = Transaction.builder()
								.from(request.getEmail())
								.to(request.getTransferee())
								.amount(request.getAmount())
								.type(TransactionTypeEnum.receive)
								.dateTime(LocalDateTime.now())
								.build();
						Mono.zip(transactionRepository.save(senderTransaction), transactionRepository.save(recipientTransaction))
							.subscribe(consumer -> {
								log.info("Transaction records saved successfully.");
							}, errorConsumer -> {
								log.error("Failed to save transaction records. Stacktrace: ", errorConsumer);
							});
					})
					.flatMap(result -> {
						return Mono.just(TransferResp.builder()
								.success(Boolean.TRUE)
								.build());
					});	
		}
	}
}
