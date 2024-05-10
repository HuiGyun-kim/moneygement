package com.room7.moneygement.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FinancialInfoDTO {
	private BigDecimal totalIncome;
	private BigDecimal totalExpense;
	private BigDecimal netAssets;
	private BigDecimal averageMonthlyExpense;
	private BigDecimal expenseChange;

	public FinancialInfoDTO(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal netAssets,
		BigDecimal averageMonthlyExpense, BigDecimal expenseChange) {
		this.totalIncome = totalIncome;
		this.totalExpense = totalExpense;
		this.netAssets = netAssets;
		this.averageMonthlyExpense = averageMonthlyExpense;
		this.expenseChange = expenseChange;
	}
}
