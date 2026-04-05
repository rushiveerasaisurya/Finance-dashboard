package com.example.demo.controller;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary(Authentication authentication) {
        String username = authentication.getName();
        boolean canViewAll = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_ANALYST"));

        DashboardSummary summary = dashboardService.getSummary(canViewAll ? null : username);
        return ResponseEntity.ok(summary);
    }
}
