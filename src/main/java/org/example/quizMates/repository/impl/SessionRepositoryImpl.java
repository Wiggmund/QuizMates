package org.example.quizMates.repository.impl;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

public class SessionRepositoryImpl implements SessionRepository {
    @Override
    public Optional<Session> findById(Long aLong) {
        return Optional.empty();
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
