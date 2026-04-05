package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class DashboardSummary {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private long totalRecords;

    private Map<String, BigDecimal> categoryExpenses;
    private List<RecordResponse> recentActivity;
    private Map<String, BigDecimal> monthlyTrends;
}