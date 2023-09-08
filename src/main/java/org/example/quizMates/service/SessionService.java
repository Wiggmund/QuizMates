package org.example.quizMates.service;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionService extends CrudService<Session, Long> {
    Optional<Session> getLastSession();
    List<Session> getHostSessions(Long hostId);
    Long createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
}
