package com.college.sms.service;

import com.college.sms.entity.Course;
import com.college.sms.entity.Subject;
import com.college.sms.entity.Teacher;
import com.college.sms.exception.ResourceNotFoundException;
import com.college.sms.repository.CourseRepository;
import com.college.sms.repository.SubjectRepository;
import com.college.sms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.delete(findCourseById(id));
    }

    public List<Subject> findAllSubjects(Long courseId) {
        if (courseId != null) return subjectRepository.findByCourseId(courseId);
        return subjectRepository.findAll();
    }

    @Transactional
    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    public Teacher findTeacher(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
    }
}
