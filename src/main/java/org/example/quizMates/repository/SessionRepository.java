package org.example.quizMates.repository;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
    void createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
}
