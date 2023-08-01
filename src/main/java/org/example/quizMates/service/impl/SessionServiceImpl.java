package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;
import org.example.quizMates.service.SessionService;

import java.util.List;

@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final static String SESSION_NOT_FOUND = "Session with id %s not found";

    @Override
    public Session findById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SESSION_NOT_FOUND, id)));
    }

    @Override
    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        sessionRepository.deleteById(id);
    }

    @Override
    public void createSession(CreateSessionDto dto) {
        sessionRepository.createSession(dto);
    }

    @Override
    public void updateSession(UpdateSessionDto dto) {
        findById(dto.getId());
        sessionRepository.updateSession(dto);
    }
}
