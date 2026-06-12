package com.college.sms.controller.api;

import com.college.sms.dto.response.ApiResponse;
import com.college.sms.entity.Mark;
import com.college.sms.entity.Mark.ExamType;
import com.college.sms.service.MarkService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
@Tag(name = "Marks")
@SecurityRequirement(name = "Bearer Authentication")
public class MarkApiController {

    private final MarkService markService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Mark>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(markService.findAll(PageRequest.of(page, size))));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<ApiResponse<Mark>> create(@RequestBody Map<String, Object> body) {
        Mark mark = markService.create(
                Long.valueOf(body.get("studentId").toString()),
                Long.valueOf(body.get("subjectId").toString()),
                ExamType.valueOf(body.get("examType").toString()),
                new BigDecimal(body.get("marksObtained").toString()),
                body.containsKey("maxMarks") ? new BigDecimal(body.get("maxMarks").toString()) : null,
                body.get("semester") != null ? Integer.valueOf(body.get("semester").toString()) : null,
                (String) body.get("academicYear"),
                (String) body.get("remarks"));
        return ResponseEntity.ok(ApiResponse.ok("Marks added", mark));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        markService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}
