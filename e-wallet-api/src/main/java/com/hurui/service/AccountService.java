package com.hurui.service;

import com.hurui.model.api.AccountRegistrationReq;
import com.hurui.model.api.AccountRegistrationResp;

import reactor.core.publisher.Mono;

public interface AccountService {

	Mono<AccountRegistrationResp> createAccount(AccountRegistrationReq request);
}
