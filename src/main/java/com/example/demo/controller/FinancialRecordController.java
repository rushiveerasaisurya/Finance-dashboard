package com.example.demo.controller;

import com.example.demo.dto.RecordRequest;
import com.example.demo.dto.RecordResponse;
import com.example.demo.service.FinancialRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {

    private final FinancialRecordService recordService;

    public FinancialRecordController(FinancialRecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public ResponseEntity<RecordResponse> createRecord(
            @Valid @RequestBody RecordRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        RecordResponse created = recordService.createRecord(request, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<RecordResponse>> getRecords(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());

        Page<RecordResponse> records = recordService.getFilteredRecords(
                isAdmin ? null : username, startDate, endDate, category, type, pageable);

        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getRecordById(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        RecordResponse record = recordService.getRecordById(id, isAdmin ? null : username);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordResponse> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody RecordRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        RecordResponse updated = recordService.updateRecord(id, request, isAdmin ? null : username);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
