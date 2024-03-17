package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api.repository.CurrUserSessionRepository;

@Service
public class CurrUserSessionService {

    private final CurrUserSessionRepository currUserSessionRepository;

    @Autowired
    public CurrUserSessionService(CurrUserSessionRepository currUserSessionRepository) {
        this.currUserSessionRepository = currUserSessionRepository;
    }

    public void saveSession(String sessionId, Long userId) {  // 변경된 부분
        currUserSessionRepository.saveSession(sessionId, userId);  // 변경된 부분
    }

    public Long getSession(String sessionId) {  // 변경된 부분
        return currUserSessionRepository.getSession(sessionId);  // 변경된 부분
    }

    public void deleteSession(String sessionId) {
        currUserSessionRepository.deleteSession(sessionId);
    }
}
