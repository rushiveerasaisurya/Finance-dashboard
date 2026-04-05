package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RecordResponse {

    private Long id;
    private String type;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate transactionDate;
    private String username;
}
