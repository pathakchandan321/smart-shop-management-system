package com.college.sms.service;

import com.college.sms.entity.Mark;
import com.college.sms.entity.Mark.ExamType;
import com.college.sms.entity.Student;
import com.college.sms.entity.Subject;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.MarkRepository;
import com.college.sms.repository.StudentRepository;
import com.college.sms.repository.SubjectRepository;
import com.college.sms.util.GradeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public Page<Mark> findAll(Pageable pageable) {
        return markRepository.findAll(pageable);
    }

    public Page<Mark> findByStudent(Long studentId, Pageable pageable) {
        return markRepository.findByStudentId(studentId, pageable);
    }

    @Transactional
    public Mark create(Long studentId, Long subjectId, ExamType examType,
                     BigDecimal obtained, BigDecimal max, Integer semester, String year, String remarks) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Mark mark = Mark.builder()
                .student(student)
                .subject(subject)
                .examType(examType)
                .marksObtained(obtained)
                .maxMarks(max != null ? max : BigDecimal.valueOf(100))
                .grade(GradeUtil.calculateGrade(obtained, max != null ? max : BigDecimal.valueOf(100)))
                .semester(semester)
                .academicYear(year)
                .remarks(remarks)
                .build();
        return markRepository.save(mark);
    }

    @Transactional
    public void delete(Long id) {
        markRepository.deleteById(id);
    }
}
