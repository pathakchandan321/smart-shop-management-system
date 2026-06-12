package com.college.sms.config;

import com.college.sms.entity.*;
import com.college.sms.entity.Role.RoleName;
import com.college.sms.entity.Student.Gender;
import com.college.sms.entity.Student.StudentStatus;
import com.college.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        if (userRepository.findByEmail("admin@college.edu").isPresent()) {
            log.info("Database already seeded, skipping sample data");
            return;
        }
        seedSampleData();
        log.info("Sample data initialized. Default password: Admin@123");
    }

    private void initRoles() {
        for (RoleName name : RoleName.values()) {
            roleRepository.findByName(name).orElseGet(() ->
                    roleRepository.save(Role.builder().name(name).build()));
        }
    }

    private void seedSampleData() {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER).orElseThrow();
        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT).orElseThrow();

        User admin = saveUser("admin@college.edu", "Admin", "User", adminRole);
        User teacher1 = saveUser("teacher1@college.edu", "Rajesh", "Kumar", teacherRole);
        User teacher2 = saveUser("teacher2@college.edu", "Priya", "Sharma", teacherRole);
        User studentUser1 = saveUser("student1@college.edu", "Amit", "Patel", studentRole);

        Course cs = courseRepository.save(Course.builder()
                .courseCode("BSC-CS").courseName("B.Sc Computer Science")
                .department("Computer Science").durationYears(3).build());
        Course bba = courseRepository.save(Course.builder()
                .courseCode("BBA").courseName("Bachelor of Business Administration")
                .department("Management").durationYears(3).build());

        Teacher t1 = teacherRepository.save(Teacher.builder()
                .user(teacher1).employeeId("EMP001").firstName("Rajesh").lastName("Kumar")
                .department("Computer Science").qualification("M.Tech").hireDate(LocalDate.of(2020, 6, 1)).build());
        teacherRepository.save(Teacher.builder()
                .user(teacher2).employeeId("EMP002").firstName("Priya").lastName("Sharma")
                .department("Management").qualification("MBA").hireDate(LocalDate.of(2019, 8, 15)).build());

        Subject s1 = subjectRepository.save(Subject.builder()
                .subjectCode("CS101").subjectName("Programming Fundamentals")
                .course(cs).credits(4).semester(1).teacher(t1).build());
        subjectRepository.save(Subject.builder()
                .subjectCode("MGT101").subjectName("Principles of Management")
                .course(bba).credits(3).semester(1).build());

        Student st1 = studentRepository.save(Student.builder()
                .user(studentUser1).studentId("STU2024001").firstName("Amit").lastName("Patel")
                .email("student1@college.edu").phone("9123456780").gender(Gender.MALE)
                .course(cs).semester(2).enrollmentDate(LocalDate.of(2023, 7, 1))
                .status(StudentStatus.ACTIVE).build());
        Student st2 = studentRepository.save(Student.builder()
                .studentId("STU2024002").firstName("Sneha").lastName("Reddy")
                .email("sneha@college.edu").course(cs).semester(2)
                .enrollmentDate(LocalDate.of(2023, 7, 1)).status(StudentStatus.ACTIVE).build());

        attendanceRepository.save(Attendance.builder().student(st1).subject(s1)
                .attendanceDate(LocalDate.now().minusDays(1)).status(Attendance.AttendanceStatus.PRESENT)
                .markedBy(t1).build());

        markRepository.save(Mark.builder().student(st1).subject(s1).examType(Mark.ExamType.MIDTERM)
                .marksObtained(BigDecimal.valueOf(42)).maxMarks(BigDecimal.valueOf(50))
                .grade("A").semester(1).academicYear("2024-25").build());

        log.info("Admin login: admin@college.edu / Admin@123");
    }

    private User saveUser(String email, String firstName, String lastName, Role role) {
        return userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode("Admin@123"))
                .firstName(firstName)
                .lastName(lastName)
                .roles(Set.of(role))
                .enabled(true)
                .build());
    }
}
