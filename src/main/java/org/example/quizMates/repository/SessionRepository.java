package org.example.quizMates.repository;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByTitle(String title);
    Optional<Session> getLastSession();
    Long getGroupScoreForSession(List<Long> studentsIds, Long sessionId);
    Long getStudentScoreForSession(Long studentId, Long sessionId);
    List<Session> getHostSessions(Long hostId);
    Session createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
}
