package com.college.sms.service;

import com.college.sms.dto.request.StudentRequest;
import com.college.sms.dto.response.StudentResponse;
import com.college.sms.entity.*;
import com.college.sms.entity.Role.RoleName;
import com.college.sms.entity.Student.StudentStatus;
import com.college.sms.exception.BadRequestException;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.*;
import com.college.sms.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final ActivityLogService activityLogService;

    @Transactional(readOnly = true)
    public Page<StudentResponse> findAll(String search, Long courseId, StudentStatus status, Pageable pageable) {
        return studentRepository.search(search, courseId, status, pageable).map(StudentResponse::from);
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return StudentResponse.from(getEntity(id));
    }

    @Transactional
    public StudentResponse create(StudentRequest req, MultipartFile photo, User currentUser, String ip) {
        if (studentRepository.existsByStudentId(req.getStudentId())) {
            throw new BadRequestException("Student ID already exists");
        }
        if (studentRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Student student = buildStudent(new Student(), req);
        if (photo != null && !photo.isEmpty()) {
            student.setPhoto(fileStorageService.storePhoto(photo));
        }

        if (req.isCreateUser()) {
            Role role = roleRepository.findByName(RoleName.ROLE_STUDENT).orElseThrow();
            User user = User.builder()
                    .email(req.getEmail())
                    .password(passwordEncoder.encode(req.getPassword() != null ? req.getPassword() : "Admin@123"))
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .roles(Set.of(role))
                    .build();
            student.setUser(userRepository.save(user));
        }

        Student saved = studentRepository.save(student);
        activityLogService.log(currentUser, "CREATE", "student", saved.getId(), "Created " + saved.getStudentId(), ip);
        return StudentResponse.from(saved);
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest req, MultipartFile photo, User currentUser, String ip) {
        Student student = getEntity(id);
        buildStudent(student, req);
        if (photo != null && !photo.isEmpty()) {
            student.setPhoto(fileStorageService.storePhoto(photo));
        }
        Student saved = studentRepository.save(student);
        activityLogService.log(currentUser, "UPDATE", "student", id, "Updated student", ip);
        return StudentResponse.from(saved);
    }

    @Transactional
    public void delete(Long id, User currentUser, String ip) {
        Student student = getEntity(id);
        studentRepository.delete(student);
        activityLogService.log(currentUser, "DELETE", "student", id, "Deleted student", ip);
    }

    public Student getEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }

    private Student buildStudent(Student student, StudentRequest req) {
        student.setStudentId(req.getStudentId());
        student.setFirstName(req.getFirstName());
        student.setLastName(req.getLastName());
        student.setEmail(req.getEmail());
        student.setPhone(req.getPhone());
        student.setDateOfBirth(req.getDateOfBirth());
        student.setGender(req.getGender());
        student.setAddress(req.getAddress());
        student.setSemester(req.getSemester());
        student.setEnrollmentDate(req.getEnrollmentDate() != null ? req.getEnrollmentDate() : LocalDate.now());
        if (req.getStatus() != null) student.setStatus(req.getStatus());
        if (req.getCourseId() != null) {
            Course course = courseRepository.findById(req.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            student.setCourse(course);
        }
        return student;
    }
}
