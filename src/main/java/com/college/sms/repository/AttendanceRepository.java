package com.college.sms.repository;

import com.college.sms.entity.Attendance;
import com.college.sms.entity.Attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Override
    @EntityGraph(attributePaths = {"student", "subject"})
    Page<Attendance> findAll(Pageable pageable);

    Optional<Attendance> findByStudentIdAndSubjectIdAndAttendanceDate(
            Long studentId, Long subjectId, LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId " +
           "AND a.status IN ('PRESENT','LATE')")
    long countPresentByStudent(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId")
    long countTotalByStudent(@Param("studentId") Long studentId);

    @EntityGraph(attributePaths = {"student", "subject"})
    Page<Attendance> findByStudentId(Long studentId, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.attendanceDate = :date AND a.status IN :statuses")
    long countByDateAndStatus(@Param("date") LocalDate date,
                              @Param("statuses") java.util.List<AttendanceStatus> statuses);
}
