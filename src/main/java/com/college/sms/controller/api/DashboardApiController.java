package com.college.sms.controller.api;

import com.college.sms.dto.response.ApiResponse;
import com.college.sms.dto.response.DashboardStatsResponse;
import com.college.sms.service.DashboardService;
import com.college.sms.service.PredictionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardApiController {

    private final DashboardService dashboardService;
    private final PredictionService predictionService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> stats() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getStats()));
    }

    @GetMapping("/predict")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> predict(@RequestParam Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok(predictionService.predict(studentId)));
    }
}
