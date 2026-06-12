package com.college.sms.repository;

import com.college.sms.entity.QrSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QrSessionRepository extends JpaRepository<QrSession, Long> {
    Optional<QrSession> findByTokenAndActiveTrueAndExpiresAtAfter(String token, LocalDateTime now);
}
