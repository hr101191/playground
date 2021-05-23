package com.hurui.unittest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.test.context.ContextConfiguration;

import com.hurui.model.api.AccountRegistrationReq;
import com.hurui.model.api.AccountRegistrationResp;
import com.hurui.model.dao.Account;
import com.hurui.repository.AccountRepository;
import com.hurui.service.AccountService;
import com.hurui.service.AccountServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@WebFluxTest
@ContextConfiguration(classes = {AccountServiceImpl.class})
public class AccountServiceUnitTest {
	
	@Autowired
	private AccountService accountService;

	@MockBean
	private AccountRepository accountRepository;
	
	@MockBean
	private R2dbcEntityTemplate r2dbcEntityTemplate;
	
	@Test
	public void createAccountTest() {
		//Prepare dummy objects
		String email = "test@gmail.com";
		AccountRegistrationReq request = AccountRegistrationReq.builder()
				.email(email)
				.build();
		
		AccountRegistrationResp expectedResponse = AccountRegistrationResp.builder()
				.success(Boolean.TRUE)
				.balance(10000.00)
				.build();
		
		Account account = Account.builder()
				.id(1L)
				.email(request.getEmail())
				.balance(10000.00)
				.build();

		//Mock db return
		when(this.r2dbcEntityTemplate.count(isA(Query.class), any()))
			.thenReturn(Mono.just(0L));
		
		when(this.accountRepository.save(any()))
			.thenReturn(Mono.just(account));
		
		//Verify result
		StepVerifier.create(accountService.createAccount(request))
			.consumeNextWith(response -> assertThat(response).isEqualTo(expectedResponse));
		
	}
}
