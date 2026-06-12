package com.college.sms.controller.web;

import com.college.sms.entity.Mark.ExamType;
import com.college.sms.repository.StudentRepository;
import com.college.sms.service.CourseService;
import com.college.sms.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/marks")
@RequiredArgsConstructor
public class MarkWebController {

    private final MarkService markService;
    private final CourseService courseService;
    private final StudentRepository studentRepository;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("marks", markService.findAll(PageRequest.of(page, 20)));
        model.addAttribute("students", studentRepository.findAll(Sort.by("firstName").ascending()));
        model.addAttribute("subjects", courseService.findAllSubjects(null));
        model.addAttribute("examTypes", ExamType.values());
        model.addAttribute("pageTitle", "Marks & Results");
        return "marks/list";
    }

    @PostMapping
    public String create(@RequestParam Long studentId,
                         @RequestParam Long subjectId,
                         @RequestParam ExamType examType,
                         @RequestParam BigDecimal marksObtained,
                         @RequestParam(defaultValue = "100") BigDecimal maxMarks,
                         RedirectAttributes redirect) {
        markService.create(studentId, subjectId, examType, marksObtained, maxMarks, null, "2024-25", null);
        redirect.addFlashAttribute("success", "Marks added");
        return "redirect:/marks";
    }
}
