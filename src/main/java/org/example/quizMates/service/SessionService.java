package org.example.quizMates.service;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;

import java.util.Optional;

public interface SessionService extends CrudService<Session, Long> {

    Optional<Session> getLastSession();
    void createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
}
