package com.hurui.service;

import com.hurui.model.api.TransferReq;
import com.hurui.model.api.TransferResp;

import reactor.core.publisher.Mono;

public interface TransferService {
	
	Mono<TransferResp> transfer(TransferReq request);
	
}
