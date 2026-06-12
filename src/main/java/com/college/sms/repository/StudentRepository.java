package com.college.sms.repository;

import com.college.sms.entity.Student;
import com.college.sms.entity.Student.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByUserId(Long userId);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    long countByStatus(StudentStatus status);

    @Query("SELECT s FROM Student s WHERE " +
           "(:search IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%',:search,'%')) " +
           "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%',:search,'%')) " +
           "OR LOWER(s.studentId) LIKE LOWER(CONCAT('%',:search,'%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%',:search,'%'))) " +
           "AND (:courseId IS NULL OR s.course.id = :courseId) " +
           "AND (:status IS NULL OR s.status = :status)")
    Page<Student> search(@Param("search") String search,
                         @Param("courseId") Long courseId,
                         @Param("status") StudentStatus status,
                         Pageable pageable);
}
