package org.example.quizMates.repository;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByTitle(String title);
    void createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
    Optional<Session> getLastSession();
}
