package com.hurui.model.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TransferReq {
	
	private String email;
	private String transferee;
	private Double amount;
	
}
