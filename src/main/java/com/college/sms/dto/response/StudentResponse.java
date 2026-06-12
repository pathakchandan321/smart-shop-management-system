package com.college.sms.dto.response;

import com.college.sms.entity.Student;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private Long courseId;
    private String courseName;
    private Integer semester;
    private LocalDate enrollmentDate;
    private String photo;
    private String status;

    public static StudentResponse from(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .studentId(s.getStudentId())
                .firstName(s.getFirstName())
                .lastName(s.getLastName())
                .email(s.getEmail())
                .phone(s.getPhone())
                .dateOfBirth(s.getDateOfBirth())
                .gender(s.getGender() != null ? s.getGender().name() : null)
                .address(s.getAddress())
                .courseId(s.getCourse() != null ? s.getCourse().getId() : null)
                .courseName(s.getCourse() != null ? s.getCourse().getCourseName() : null)
                .semester(s.getSemester())
                .enrollmentDate(s.getEnrollmentDate())
                .photo(s.getPhoto())
                .status(s.getStatus() != null ? s.getStatus().name() : null)
                .build();
    }
}
