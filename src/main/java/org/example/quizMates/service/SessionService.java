package org.example.quizMates.service;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;

public interface SessionService extends CrudService<Session, Long> {
    void createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
}
