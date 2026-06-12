package com.college.sms.service;

import com.college.sms.dto.response.DashboardStatsResponse;
import com.college.sms.entity.Attendance.AttendanceStatus;
import com.college.sms.entity.Student.StudentStatus;
import com.college.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        long totalToday = attendanceRepository.countByDateAndStatus(LocalDate.now(),
                List.of(AttendanceStatus.PRESENT, AttendanceStatus.LATE, AttendanceStatus.ABSENT));
        long presentToday = attendanceRepository.countByDateAndStatus(LocalDate.now(),
                List.of(AttendanceStatus.PRESENT, AttendanceStatus.LATE));
        double rate = totalToday > 0 ? (presentToday * 100.0 / totalToday) : 0;

        List<Map<String, Object>> recent = studentRepository.findAll(PageRequest.of(0, 5))
                .getContent().stream().map(s -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", s.getId());
                    m.put("studentId", s.getStudentId());
                    m.put("name", s.getFirstName() + " " + s.getLastName());
                    m.put("course", s.getCourse() != null ? s.getCourse().getCourseName() : "N/A");
                    return m;
                }).collect(Collectors.toList());

        List<Map<String, Object>> courseDist = courseRepository.findAll().stream().map(c -> {
            Map<String, Object> m = new HashMap<>();
            m.put("courseName", c.getCourseName());
            m.put("count", studentRepository.search(null, c.getId(), null, PageRequest.of(0, 1)).getTotalElements());
            return m;
        }).collect(Collectors.toList());

        return DashboardStatsResponse.builder()
                .totalStudents(studentRepository.countByStatus(StudentStatus.ACTIVE))
                .totalTeachers(teacherRepository.count())
                .totalCourses(courseRepository.count())
                .totalSubjects(subjectRepository.count())
                .attendanceRateToday(rate)
                .recentStudents(recent)
                .courseDistribution(courseDist)
                .build();
    }
}
