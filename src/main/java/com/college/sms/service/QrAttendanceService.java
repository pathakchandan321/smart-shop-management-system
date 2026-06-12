package com.college.sms.service;

import com.college.sms.entity.*;
import com.college.sms.entity.Attendance.AttendanceStatus;
import com.college.sms.exception.BadRequestException;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrAttendanceService {

    private final QrSessionRepository qrSessionRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public Map<String, Object> createSession(Long subjectId, Long teacherId, int minutes) throws Exception {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expires = LocalDateTime.now().plusMinutes(minutes);

        qrSessionRepository.save(QrSession.builder()
                .subject(subject)
                .teacher(teacher)
                .token(token)
                .expiresAt(expires)
                .active(true)
                .build());

        String qrContent = "{\"token\":\"" + token + "\",\"subjectId\":" + subjectId + "}";
        String qrBase64 = generateQrBase64(qrContent);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("expiresAt", expires);
        result.put("qrImage", qrBase64);
        return result;
    }

    @Transactional
    public void scan(String token, Long studentId) {
        QrSession session = qrSessionRepository
                .findByTokenAndActiveTrueAndExpiresAtAfter(token, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("Invalid or expired QR session"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Attendance att = attendanceRepository
                .findByStudentIdAndSubjectIdAndAttendanceDate(
                        studentId, session.getSubject().getId(), LocalDate.now())
                .orElse(Attendance.builder()
                        .student(student)
                        .subject(session.getSubject())
                        .attendanceDate(LocalDate.now())
                        .build());

        att.setStatus(AttendanceStatus.PRESENT);
        att.setMarkedBy(session.getTeacher());
        att.setQrToken(token);
        attendanceRepository.save(att);
    }

    private String generateQrBase64(String content) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
