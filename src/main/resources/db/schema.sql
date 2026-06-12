-- Student Management System - MySQL Schema Reference
-- JPA ddl-auto=update will create tables automatically
-- Use this script for manual setup if preferred

CREATE DATABASE IF NOT EXISTS student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_management;

-- Tables are managed by Hibernate entities:
-- roles, users, user_roles, students, teachers, courses, subjects,
-- attendance, marks, results, activity_logs, notifications, qr_sessions
