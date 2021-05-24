package com.hurui.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hurui.handler.AccountHandler;
import com.hurui.model.api.AccountRegistrationReq;
import com.hurui.model.api.AccountRegistrationResp;

import static org.springdoc.core.fn.builders.apiresponse.Builder.*;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {

	@Bean
	public RouterFunction<ServerResponse> routeAccountRequests(AccountHandler accountHandler) {
		return route().POST("/api/account", accept(MediaType.APPLICATION_JSON), accountHandler::register, ops -> ops.tag("Account")
				.operationId("register")
				.summary("Register a new wallet account")
				.requestBody(requestBodyBuilder()
						.content(contentBuilder()
								.mediaType(MediaType.APPLICATION_JSON_VALUE)
								.schema(schemaBuilder().implementation(AccountRegistrationReq.class)))
				)
				.response(responseBuilder().responseCode("200").description("success").implementation(AccountRegistrationResp.class))
				.response(responseBuilder().responseCode("500").description("Internal Server Error")))
				.build();			
	}
}
