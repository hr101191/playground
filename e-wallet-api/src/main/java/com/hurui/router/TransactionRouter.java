package com.hurui.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hurui.handler.TransactionHandler;
import com.hurui.model.api.TransactionReq;
import com.hurui.model.api.TransactionResp;

import static org.springdoc.core.fn.builders.apiresponse.Builder.*;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TransactionRouter {

	@Bean
	public RouterFunction<ServerResponse> routeTransactionRequests(TransactionHandler transactionHandler) {
		return route().POST("/api/transaction", accept(MediaType.APPLICATION_JSON), transactionHandler::getTransactionsByEmail, ops -> ops.tag("Transaction")
				.operationId("getAllTransactions")
				.summary("Retrieve all transactions by email")
				.requestBody(requestBodyBuilder()
						.content(contentBuilder()
								.mediaType(MediaType.APPLICATION_JSON_VALUE)
								.schema(schemaBuilder().implementation(TransactionReq.class)))
				)
				.response(responseBuilder().responseCode("200").description("success").implementation(TransactionResp.class))
				.response(responseBuilder().responseCode("500").description("Internal Server Error")))
				.build();			
	}
}
