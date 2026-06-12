package com.college.sms.controller.web;

import com.college.sms.entity.Course;
import com.college.sms.entity.Subject;
import com.college.sms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseWebController {

    private final CourseService courseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        model.addAttribute("subjects", courseService.findAllSubjects(null));
        model.addAttribute("pageTitle", "Courses & Subjects");
        return "courses/list";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createCourse(@ModelAttribute Course course, RedirectAttributes redirect) {
        courseService.saveCourse(course);
        redirect.addFlashAttribute("success", "Course created");
        return "redirect:/courses";
    }

    @PostMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    public String createSubject(@ModelAttribute Subject subject,
                                @RequestParam("course.id") Long courseId,
                                RedirectAttributes redirect) {
        subject.setCourse(courseService.findCourseById(courseId));
        courseService.saveSubject(subject);
        redirect.addFlashAttribute("success", "Subject created");
        return "redirect:/courses";
    }
}
