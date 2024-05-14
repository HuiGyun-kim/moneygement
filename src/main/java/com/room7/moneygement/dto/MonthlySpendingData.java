package com.room7.moneygement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MonthlySpendingData {
	@JsonProperty("year")
	private int year;
	@JsonProperty("month")
	private int month;
	@JsonProperty("income")
	private Long income;
	@JsonProperty("expense")
	private Long expense;

	public MonthlySpendingData(int year, int month, Long totalByLedgerAndTypeFalse,Long totalByLedgerAndTypeTrue) {
		this.year = year;
		this.month = month;
		this.income = totalByLedgerAndTypeFalse;
		this.expense = totalByLedgerAndTypeTrue;
	}
}
