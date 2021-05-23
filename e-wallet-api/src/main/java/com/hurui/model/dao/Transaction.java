package com.hurui.model.dao;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.hurui.utils.TransactionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Table("TRANSACTION")
@Data
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Transaction {
	
	@Id
	@GeneratedValue
	@Column(value = "ID")
	private Long id;
	
	@Column(value = "EMAIL_FROM")
	private String from;
	
	@Column(value = "EMAIL_TO")
	private String to;
	
	@Column(value = "TRANSFER_TYPE")
	@Enumerated(EnumType.STRING)
	private TransactionTypeEnum type;
	
	@Column(value = "AMOUNT")
	private Double amount;
	
	@Column(value = "TRANSACTION_DATETIME")
	private LocalDateTime dateTime;
}
