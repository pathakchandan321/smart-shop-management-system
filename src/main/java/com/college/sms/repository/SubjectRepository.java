package com.college.sms.repository;

import com.college.sms.entity.Subject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Override
    @EntityGraph(attributePaths = {"course"})
    List<Subject> findAll();

    @EntityGraph(attributePaths = {"course"})
    List<Subject> findByCourseId(Long courseId);
}
