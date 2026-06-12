package com.college.sms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalStudents;
    private long totalTeachers;
    private long totalCourses;
    private long totalSubjects;
    private double attendanceRateToday;
    private List<Map<String, Object>> recentStudents;
    private List<Map<String, Object>> courseDistribution;
}
