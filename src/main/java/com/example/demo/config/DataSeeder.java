package com.example.demo.config;

import com.example.demo.entity.FinancialRecord;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.enums.TransactionType;
import com.example.demo.repository.FinancialRecordRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataSeeder {

        @Bean
        public CommandLineRunner loadData(UserRepository userRepository,
                        FinancialRecordRepository recordRepository,
                        PasswordEncoder passwordEncoder) {

                return args -> {

                        User admin = User.builder()
                                        .username("admin")
                                        .password(passwordEncoder.encode("admin123"))
                                        .email("admin@finance.com")
                                        .role(Role.ADMIN)
                                        .build();

                        User analyst = User.builder()
                                        .username("analyst")
                                        .password(passwordEncoder.encode("analyst123"))
                                        .email("analyst@finance.com")
                                        .role(Role.ANALYST)
                                        .build();

                        User viewer = User.builder()
                                        .username("viewer")
                                        .password(passwordEncoder.encode("viewer123"))
                                        .email("viewer@finance.com")
                                        .role(Role.VIEWER)
                                        .build();

                        userRepository.save(admin);
                        userRepository.save(analyst);
                        userRepository.save(viewer);

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.INCOME)
                                        .amount(new BigDecimal("5000.00"))
                                        .category("Salary")
                                        .description("Monthly salary - April")
                                        .transactionDate(LocalDate.of(2026, 4, 1))
                                        .user(admin)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.EXPENSE)
                                        .amount(new BigDecimal("1200.00"))
                                        .category("Rent")
                                        .description("Office rent")
                                        .transactionDate(LocalDate.of(2026, 4, 1))
                                        .user(admin)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.EXPENSE)
                                        .amount(new BigDecimal("150.00"))
                                        .category("Utilities")
                                        .description("Electricity bill")
                                        .transactionDate(LocalDate.of(2026, 4, 2))
                                        .user(admin)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.INCOME)
                                        .amount(new BigDecimal("3500.00"))
                                        .category("Salary")
                                        .description("Monthly salary - April")
                                        .transactionDate(LocalDate.of(2026, 4, 1))
                                        .user(analyst)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.EXPENSE)
                                        .amount(new BigDecimal("45.99"))
                                        .category("Groceries")
                                        .description("Weekly groceries")
                                        .transactionDate(LocalDate.of(2026, 4, 2))
                                        .user(analyst)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.INCOME)
                                        .amount(new BigDecimal("800.00"))
                                        .category("Freelance")
                                        .description("Web design project")
                                        .transactionDate(LocalDate.of(2026, 4, 3))
                                        .user(analyst)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.INCOME)
                                        .amount(new BigDecimal("2800.00"))
                                        .category("Salary")
                                        .description("Monthly salary - April")
                                        .transactionDate(LocalDate.of(2026, 4, 1))
                                        .user(viewer)
                                        .build());

                        recordRepository.save(FinancialRecord.builder()
                                        .type(TransactionType.EXPENSE)
                                        .amount(new BigDecimal("600.00"))
                                        .category("Rent")
                                        .description("Home rent")
                                        .transactionDate(LocalDate.of(2026, 4, 1))
                                        .user(viewer)
                                        .build());

                        System.out.println("═══════════════════════════════════════════════");
                        System.out.println("  ✅ Sample data loaded successfully!");
                        System.out.println("  📊 3 users and 8 financial records created");
                        System.out.println("");
                        System.out.println("  Login credentials:");
                        System.out.println("    admin   / admin123   (ADMIN)");
                        System.out.println("    analyst / analyst123 (ANALYST)");
                        System.out.println("    viewer  / viewer123  (VIEWER)");
                        System.out.println("");
                        System.out.println("  🔗 Swagger UI: http://localhost:8080/swagger-ui.html");
                        System.out.println("  🔗 H2 Console: http://localhost:8080/h2-console");
                        System.out.println("═══════════════════════════════════════════════");
                };
        }
}
