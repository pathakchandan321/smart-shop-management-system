package com.college.sms.controller.api;

import com.college.sms.dto.response.ApiResponse;
import com.college.sms.entity.Course;
import com.college.sms.entity.Subject;
import com.college.sms.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses")
@SecurityRequirement(name = "Bearer Authentication")
public class CourseApiController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(courseService.findAllCourses()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Course>> create(@RequestBody Course course) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.saveCourse(course)));
    }

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<Subject>>> subjects(@RequestParam(required = false) Long courseId) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.findAllSubjects(courseId)));
    }

    @PostMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Subject>> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.saveSubject(subject)));
    }
}
