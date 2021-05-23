package com.hurui.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class AccountRegistrationResp {
	
	private Boolean success;
	private Double balance;
	
}
