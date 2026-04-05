package com.example.demo.service;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.entity.FinancialRecord;
import com.example.demo.enums.TransactionType;
import com.example.demo.repository.FinancialRecordRepository;
import com.example.demo.dto.RecordResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public DashboardService(FinancialRecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public DashboardSummary getSummary(String username) {
        List<FinancialRecord> allRecords;
        
        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            allRecords = recordRepository.findByUserId(user.getId());
        } else {
            allRecords = recordRepository.findAll();
        }

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

        Map<String, BigDecimal> categoryExpenses = allRecords.stream()
                .filter(r -> r.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        FinancialRecord::getCategory,
                        Collectors.mapping(FinancialRecord::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        Map<String, BigDecimal> monthlyTrends = allRecords.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getTransactionDate().getYear() + "-" + String.format("%02d", r.getTransactionDate().getMonthValue()),
                        Collectors.mapping(
                                r -> r.getType() == TransactionType.INCOME ? r.getAmount() : r.getAmount().negate(),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        List<RecordResponse> recentActivity = allRecords.stream()
                .sorted((a, b) -> b.getTransactionDate().compareTo(a.getTransactionDate()))
                .limit(10)
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new DashboardSummary(totalIncome, totalExpense, netBalance, totalRecords, categoryExpenses, recentActivity, monthlyTrends);
    }
    
    private RecordResponse toResponse(FinancialRecord record) {
        return new RecordResponse(
                record.getId(),
                record.getType().name(),
                record.getAmount(),
                record.getCategory(),
                record.getDescription(),
                record.getTransactionDate(),
                record.getUser().getUsername());
    }
}
