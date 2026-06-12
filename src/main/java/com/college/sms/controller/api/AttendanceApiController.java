package com.college.sms.controller.api;

import com.college.sms.dto.response.ApiResponse;
import com.college.sms.entity.Attendance;
import com.college.sms.entity.Attendance.AttendanceStatus;
import com.college.sms.service.AttendanceService;
import com.college.sms.service.QrAttendanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance")
@SecurityRequirement(name = "Bearer Authentication")
public class AttendanceApiController {

    private final AttendanceService attendanceService;
    private final QrAttendanceService qrAttendanceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Page<Attendance>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                attendanceService.findAll(PageRequest.of(page, size))));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Attendance>> mark(@RequestBody Map<String, Object> body) {
        Attendance att = attendanceService.mark(
                Long.valueOf(body.get("studentId").toString()),
                Long.valueOf(body.get("subjectId").toString()),
                body.containsKey("date") ? LocalDate.parse(body.get("date").toString()) : LocalDate.now(),
                AttendanceStatus.valueOf(body.get("status").toString()),
                body.get("teacherId") != null ? Long.valueOf(body.get("teacherId").toString()) : null,
                (String) body.get("remarks"));
        return ResponseEntity.ok(ApiResponse.ok("Attendance marked", att));
    }

    @PostMapping("/qr/create")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createQr(@RequestBody Map<String, Object> body) throws Exception {
        return ResponseEntity.ok(ApiResponse.ok(qrAttendanceService.createSession(
                Long.valueOf(body.get("subjectId").toString()),
                Long.valueOf(body.get("teacherId").toString()),
                body.containsKey("minutes") ? Integer.parseInt(body.get("minutes").toString()) : 15)));
    }

    @PostMapping("/qr/scan")
    public ResponseEntity<ApiResponse<Void>> scanQr(@RequestBody Map<String, Object> body) throws Exception {
        qrAttendanceService.scan(body.get("token").toString(), Long.valueOf(body.get("studentId").toString()));
        return ResponseEntity.ok(ApiResponse.ok("Attendance marked via QR", null));
    }
}
