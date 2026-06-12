package com.college.sms.service;

import com.college.sms.entity.*;
import com.college.sms.entity.Attendance.AttendanceStatus;
import com.college.sms.exception.BadRequestException;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public Page<Attendance> findAll(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    @Transactional
    public Attendance mark(Long studentId, Long subjectId, LocalDate date,
                           AttendanceStatus status, Long teacherId, String remarks) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Attendance attendance = attendanceRepository
                .findByStudentIdAndSubjectIdAndAttendanceDate(studentId, subjectId, date)
                .orElse(Attendance.builder()
                        .student(student)
                        .subject(subject)
                        .attendanceDate(date)
                        .build());

        attendance.setStatus(status);
        attendance.setRemarks(remarks);
        if (teacherId != null) {
            attendance.setMarkedBy(teacherRepository.findById(teacherId).orElse(null));
        }
        return attendanceRepository.save(attendance);
    }

    public Map<String, Long> getStats(Long studentId) {
        long present = attendanceRepository.countPresentByStudent(studentId);
        long total = attendanceRepository.countTotalByStudent(studentId);
        return Map.of("present", present, "absent", total - present, "total", total);
    }
}
