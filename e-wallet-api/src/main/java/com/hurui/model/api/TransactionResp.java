package com.hurui.model.api;

import java.util.List;

import com.hurui.model.dao.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionResp {
	
	private Boolean success;
	private List<Transaction> transactions;
	
}
