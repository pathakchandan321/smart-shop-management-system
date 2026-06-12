package com.college.sms.repository;

import com.college.sms.entity.Mark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Override
    @EntityGraph(attributePaths = {"student", "subject"})
    Page<Mark> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"student", "subject"})
    Page<Mark> findByStudentId(Long studentId, Pageable pageable);

    List<Mark> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Mark> findTop10ByStudentIdOrderByCreatedAtDesc(Long studentId);
}
