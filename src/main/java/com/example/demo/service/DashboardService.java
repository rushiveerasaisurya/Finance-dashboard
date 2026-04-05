package com.example.demo.service;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.entity.FinancialRecord;
import com.example.demo.enums.TransactionType;
import com.example.demo.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final FinancialRecordRepository recordRepository;

    public DashboardService(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public DashboardSummary getSummary() {
        List<FinancialRecord> allRecords = recordRepository.findAll();
        BigDecimal totalIncome = allRecords.stream()
                .filter(r -> r.getType() == TransactionType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = allRecords.stream()
                .filter(r -> r.getType() == TransactionType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        long totalRecords = allRecords.size();

        return new DashboardSummary(totalIncome, totalExpense, netBalance, totalRecords);
    }
}
