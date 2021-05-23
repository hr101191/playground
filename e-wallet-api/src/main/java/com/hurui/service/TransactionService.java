package com.hurui.service;

import com.hurui.model.api.TransactionReq;
import com.hurui.model.api.TransactionResp;

import reactor.core.publisher.Mono;

public interface TransactionService {

	Mono<TransactionResp> getTransactionsByEmail(TransactionReq request);
	
}
