package com.college.sms.controller.web;

import com.college.sms.repository.StudentRepository;
import com.college.sms.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/predictions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
public class PredictionWebController {

    private final PredictionService predictionService;
    private final StudentRepository studentRepository;

    @GetMapping
    public String page(@RequestParam(required = false) Long studentId, Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("pageTitle", "AI Predictions");
        if (studentId != null) {
            model.addAttribute("prediction", predictionService.predict(studentId));
            model.addAttribute("selectedStudentId", studentId);
        }
        return "predictions/index";
    }
}
