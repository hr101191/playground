package com.hurui.model.dao;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.Email;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Table("ACCOUNT")
@Data
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Account {
	
	@Id
	@GeneratedValue
	@Column(value = "ID")
	private Long id;
	
	@Email
	@Column(value = "EMAIL")
	private String email;
	
	@Column(value = "BALANCE")
	private Double balance;

}
