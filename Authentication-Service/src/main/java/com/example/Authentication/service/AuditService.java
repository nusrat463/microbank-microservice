package com.example.Authentication.service;

import com.example.Authentication.entity.AuditLog;
import com.example.Authentication.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void log(String username, String action, String status, String message) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setStatus(status);
        log.setMessage(message);
        auditLogRepository.save(log);
    }
}
