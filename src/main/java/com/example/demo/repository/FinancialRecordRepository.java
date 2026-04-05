package com.example.demo.repository;

import com.example.demo.entity.FinancialRecord;
import com.example.demo.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long>, JpaSpecificationExecutor<FinancialRecord> {
    List<FinancialRecord> findByUserId(Long userId);

    List<FinancialRecord> findByType(TransactionType type);

    List<FinancialRecord> findByUserIdAndType(Long userId, TransactionType type);
}
