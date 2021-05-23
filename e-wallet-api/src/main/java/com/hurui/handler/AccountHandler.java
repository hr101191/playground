package com.hurui.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hurui.model.api.AccountRegistrationReq;
import com.hurui.model.api.AccountRegistrationResp;
import com.hurui.service.AccountService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountHandler {
	
	private final AccountService accountService;

	public Mono<ServerResponse> register(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(AccountRegistrationReq.class)
				.map(requestBody -> accountService.createAccount(requestBody))
				.flatMap(responseBody -> ServerResponse.ok().body(responseBody, AccountRegistrationResp.class));
	}
}
