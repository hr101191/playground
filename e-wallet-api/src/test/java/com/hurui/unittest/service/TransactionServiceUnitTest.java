package com.hurui.unittest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.test.context.ContextConfiguration;

import com.hurui.model.api.TransactionReq;
import com.hurui.model.api.TransactionResp;
import com.hurui.service.TransactionService;
import com.hurui.service.TransactionServiceImpl;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

@WebFluxTest
@ContextConfiguration(classes = {TransactionServiceImpl.class})
public class TransactionServiceUnitTest {

	@Autowired
	private TransactionService transactionService;
	
	@MockBean
	private R2dbcEntityTemplate r2dbcEntityTemplate;
	
	@SuppressWarnings("unchecked")
	@Test
	public void getTransactionsByEmailTest() {
		//Prepare dummy objects
		String email = "test@gmail.com";
		TransactionReq request = TransactionReq.builder()
				.email(email)
				.build();
		
		TransactionResp transactionResp = TransactionResp.builder()
				.success(Boolean.TRUE)
				.transactions(new ArrayList<>())
				.build();
		
		//Mock db return
		when(this.r2dbcEntityTemplate.select(isA(Query.class), any()))
			.thenReturn(Flux.empty(), Flux.empty());
		
		//Verify result
		StepVerifier.create(transactionService.getTransactionsByEmail(request))
			.consumeNextWith(response -> assertThat(response).isEqualTo(transactionResp));
	}
}