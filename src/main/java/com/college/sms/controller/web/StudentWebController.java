package com.college.sms.controller.web;

import com.college.sms.dto.request.StudentRequest;
import com.college.sms.entity.Student.StudentStatus;
import com.college.sms.repository.UserRepository;
import com.college.sms.service.CourseService;
import com.college.sms.service.ExportService;
import com.college.sms.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentWebController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final ExportService exportService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) Long courseId,
                       @RequestParam(required = false) StudentStatus status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        model.addAttribute("students", studentService.findAll(search, courseId, status,
                PageRequest.of(page, 10, Sort.by("createdAt").descending())));
        model.addAttribute("courses", courseService.findAllCourses());
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Students");
        return "students/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("student", new StudentRequest());
        model.addAttribute("courses", courseService.findAllCourses());
        model.addAttribute("pageTitle", "Add Student");
        return "students/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@ModelAttribute StudentRequest student,
                         @RequestParam(required = false) MultipartFile photo,
                         @AuthenticationPrincipal UserDetails userDetails,
                         HttpServletRequest request,
                         RedirectAttributes redirect) {
        var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        studentService.create(student, photo, user, request.getRemoteAddr());
        redirect.addFlashAttribute("success", "Student created successfully");
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        var response = studentService.findById(id);
        StudentRequest req = new StudentRequest();
        req.setStudentId(response.getStudentId());
        req.setFirstName(response.getFirstName());
        req.setLastName(response.getLastName());
        req.setEmail(response.getEmail());
        req.setPhone(response.getPhone());
        req.setCourseId(response.getCourseId());
        req.setSemester(response.getSemester());
        model.addAttribute("student", req);
        model.addAttribute("studentId", id);
        model.addAttribute("courses", courseService.findAllCourses());
        model.addAttribute("pageTitle", "Edit Student");
        return "students/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id,
                         @ModelAttribute StudentRequest student,
                         @RequestParam(required = false) MultipartFile photo,
                         @AuthenticationPrincipal UserDetails userDetails,
                         HttpServletRequest request,
                         RedirectAttributes redirect) {
        var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        studentService.update(id, student, photo, user, request.getRemoteAddr());
        redirect.addFlashAttribute("success", "Student updated successfully");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails,
                         HttpServletRequest request,
                         RedirectAttributes redirect) {
        var user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        studentService.delete(id, user, request.getRemoteAddr());
        redirect.addFlashAttribute("success", "Student deleted");
        return "redirect:/students";
    }

    @GetMapping("/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportPdf(HttpServletResponse response) throws Exception {
        byte[] data = exportService.exportStudentsPdf();
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.pdf");
        response.getOutputStream().write(data);
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportExcel(HttpServletResponse response) throws Exception {
        byte[] data = exportService.exportStudentsExcel();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.xlsx");
        response.getOutputStream().write(data);
    }
}
