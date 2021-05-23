package com.hurui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hurui.handler.TransferHandler;
import com.hurui.model.api.TransferReq;
import com.hurui.model.api.TransferResp;

import static org.springdoc.core.fn.builders.apiresponse.Builder.*;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TransferRouter {

	@Bean
	public RouterFunction<ServerResponse> routeTransferRequests(TransferHandler transferHandler) {
		return route().POST("/api/transfer", accept(MediaType.APPLICATION_JSON), transferHandler::transfer, ops -> ops.tag("Transfer")
				.operationId("register")
				.summary("Transfer money to another account")
				.requestBody(requestBodyBuilder()
						.content(contentBuilder()
								.mediaType(MediaType.APPLICATION_JSON_VALUE)
								.schema(schemaBuilder().implementation(TransferReq.class)))
				)
				.response(responseBuilder().responseCode("200").description("success").implementation(TransferResp.class))
				.response(responseBuilder().responseCode("500").description("Internal Server Error")))
				.build();			
	}
}
