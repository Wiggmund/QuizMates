package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;
import org.example.quizMates.service.SessionService;

import java.util.List;

@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    @Override
    public Session findById(Long aLong) {
        return null;
    }

    @Override
    public List<Session> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createSession(CreateSessionDto dto) {

    }

    @Override
    public void updateSession(UpdateSessionDto dto) {

    }
}