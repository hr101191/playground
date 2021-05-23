package com.hurui.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hurui.model.api.TransactionReq;
import com.hurui.model.api.TransactionResp;
import com.hurui.service.TransactionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TransactionHandler {

	private final TransactionService transactionService;
	
	public Mono<ServerResponse> getTransactionsByEmail(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(TransactionReq.class)
				.map(transactionReq -> transactionService.getTransactionsByEmail(transactionReq))
				.flatMap(transactionResp -> ServerResponse.ok().body(transactionResp, TransactionResp.class));
	}
}
