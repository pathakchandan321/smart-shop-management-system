package com.college.sms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    private Integer semester;

    @Column(name = "total_marks", precision = 8, scale = 2)
    private BigDecimal totalMarks;

    @Column(name = "obtained_marks", precision = 8, scale = 2)
    private BigDecimal obtainedMarks;

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(length = 5)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ResultStatus status = ResultStatus.PASS;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum ResultStatus { PASS, FAIL, PENDING }
}
