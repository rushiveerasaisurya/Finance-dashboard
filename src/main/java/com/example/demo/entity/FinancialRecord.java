package com.example.demo.entity;

import com.example.demo.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records", indexes = {
                @Index(name = "idx_fr_user_id", columnList = "user_id"),
                @Index(name = "idx_fr_type", columnList = "type"),
                @Index(name = "idx_fr_transaction_date", columnList = "transaction_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecord {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "Transaction type is required")
        @Enumerated(EnumType.STRING)
        @Column(name = "type", nullable = false, length = 20)
        private TransactionType type;

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 13, fraction = 2, message = "Amount must have at most 13 integer digits and 2 decimal places")
        @Column(nullable = false, precision = 15, scale = 2)
        private BigDecimal amount;

        @NotBlank(message = "Category is required")
        @Size(max = 50, message = "Category must not exceed 50 characters")
        @Column(nullable = false, length = 50)
        private String category;

        @Size(max = 255, message = "Description must not exceed 255 characters")
        private String description;

        @NotNull(message = "Transaction date is required")
        @PastOrPresent(message = "Transaction date cannot be in the future")
        @Column(name = "transaction_date", nullable = false)
        private LocalDate transactionDate;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fr_user"))
        private User user;

        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;
}
