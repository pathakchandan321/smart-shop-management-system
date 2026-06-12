package com.college.sms.service;

import com.college.sms.entity.ActivityLog;
import com.college.sms.entity.User;
import com.college.sms.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public void log(User user, String action, String entityType, Long entityId, String details, String ip) {
        activityLogRepository.save(ActivityLog.builder()
                .user(user)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .ipAddress(ip)
                .build());
    }

    public Page<ActivityLog> getAll(Pageable pageable) {
        return activityLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
