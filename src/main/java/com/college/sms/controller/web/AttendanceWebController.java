package com.college.sms.controller.web;

import com.college.sms.entity.Attendance.AttendanceStatus;
import com.college.sms.repository.StudentRepository;
import com.college.sms.service.AttendanceService;
import com.college.sms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
public class AttendanceWebController {

    private final AttendanceService attendanceService;
    private final CourseService courseService;
    private final StudentRepository studentRepository;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("attendance", attendanceService.findAll(PageRequest.of(page, 20)));
        model.addAttribute("students", studentRepository.findAll(Sort.by("firstName").ascending()));
        model.addAttribute("subjects", courseService.findAllSubjects(null));
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("pageTitle", "Attendance");
        return "attendance/list";
    }

    @PostMapping("/mark")
    public String mark(@RequestParam Long studentId,
                       @RequestParam Long subjectId,
                       @RequestParam AttendanceStatus status,
                       @RequestParam(required = false) Long teacherId,
                       RedirectAttributes redirect) {
        attendanceService.mark(studentId, subjectId, LocalDate.now(), status, teacherId, null);
        redirect.addFlashAttribute("success", "Attendance marked");
        return "redirect:/attendance";
    }
}
