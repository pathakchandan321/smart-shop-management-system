package com.college.sms.dto.request;

import com.college.sms.entity.Student.Gender;
import com.college.sms.entity.Student.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank @Email
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private Long courseId;
    private Integer semester;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    private boolean createUser;
    private String password;
}
