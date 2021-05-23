package com.hurui.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hurui.model.api.TransferReq;
import com.hurui.model.api.TransferResp;
import com.hurui.service.TransferService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransferHandler {

	private final TransferService transferService;
	
	public Mono<ServerResponse> transfer(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(TransferReq.class)
				.map(transferReq -> transferService.transfer(transferReq))
				.flatMap(transferResp -> ServerResponse.ok().body(transferResp, TransferResp.class));
				
	}
}
