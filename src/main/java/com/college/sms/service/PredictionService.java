package com.college.sms.service;

import com.college.sms.entity.Mark;
import com.college.sms.entity.Student;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.AttendanceRepository;
import com.college.sms.repository.MarkRepository;
import com.college.sms.repository.StudentRepository;
import com.college.sms.util.GradeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;

    public Map<String, Object> predict(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Mark> marks = markRepository.findTop10ByStudentIdOrderByCreatedAtDesc(studentId);
        double marksAvg = 65;
        double trend = 0;
        if (!marks.isEmpty()) {
            List<Double> pcts = marks.stream()
                    .map(m -> m.getMarksObtained().multiply(BigDecimal.valueOf(100))
                            .divide(m.getMaxMarks(), 2, RoundingMode.HALF_UP).doubleValue())
                    .toList();
            marksAvg = pcts.stream().mapToDouble(Double::doubleValue).average().orElse(65);
            if (pcts.size() >= 2) trend = pcts.get(0) - pcts.get(pcts.size() - 1);
        }

        long present = attendanceRepository.countPresentByStudent(studentId);
        long total = attendanceRepository.countTotalByStudent(studentId);
        double attendanceRate = total > 0 ? (present * 100.0 / total) : 75;

        double predicted = Math.min(100, Math.max(0,
                marksAvg * 0.6 + attendanceRate * 0.25 + (50 + Math.min(10, trend * 0.5)) * 0.15));
        BigDecimal pct = BigDecimal.valueOf(predicted).setScale(2, RoundingMode.HALF_UP);
        String grade = GradeUtil.calculateGradeFromPercentage(pct);
        int confidence = Math.min(95, 60 + marks.size() * 5 + (total > 5 ? 15 : 0));

        Map<String, Object> factors = new HashMap<>();
        factors.put("marksAverage", Math.round(marksAvg * 100) / 100.0);
        factors.put("attendanceRate", Math.round(attendanceRate * 100) / 100.0);
        factors.put("trend", Math.round(trend * 100) / 100.0);
        factors.put("dataPoints", marks.size());

        Map<String, Object> result = new HashMap<>();
        result.put("studentName", student.getFirstName() + " " + student.getLastName());
        result.put("predictedGrade", grade);
        result.put("predictedPercentage", pct);
        result.put("confidenceScore", confidence);
        result.put("factors", factors);
        result.put("recommendation", predicted >= 60
                ? "Student is on track. Maintain current study habits."
                : "Student may need additional support. Consider remedial sessions.");
        return result;
    }
}
