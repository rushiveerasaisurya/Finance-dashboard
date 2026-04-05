package com.example.demo.service;

import com.example.demo.dto.RecordRequest;
import com.example.demo.dto.RecordResponse;
import com.example.demo.entity.FinancialRecord;
import com.example.demo.entity.User;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FinancialRecordRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("null")
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public FinancialRecordService(FinancialRecordRepository recordRepository,
            UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public RecordResponse createRecord(RecordRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        TransactionType type;
        try {
            type = TransactionType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid type: " + request.getType() + ". Must be INCOME or EXPENSE");
        }

        FinancialRecord record = FinancialRecord.builder()
                .type(type)
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .user(user)
                .build();

        FinancialRecord saved = recordRepository.save(record);

        return toResponse(saved);
    }

    public List<RecordResponse> getAllRecords() {
        return recordRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<RecordResponse> getRecordsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return recordRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RecordResponse getRecordById(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return toResponse(record);
    }

    public RecordResponse updateRecord(Long id, RecordRequest request) {

        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        TransactionType type;
        try {
            type = TransactionType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid type: " + request.getType() + ". Must be INCOME or EXPENSE");
        }

        record.setType(type);
        record.setAmount(request.getAmount());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setTransactionDate(request.getTransactionDate());

        FinancialRecord updated = recordRepository.save(record);

        return toResponse(updated);
    }

    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Record not found with id: " + id);
        }
        recordRepository.deleteById(id);
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
