package org.example.quizMates.service;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.model.Session;
import org.example.quizMates.model.Student;

import java.util.List;
import java.util.Optional;

public interface SessionService extends CrudService<Session, Long> {
    Optional<Session> getLastSession();
    List<Session> getHostSessions(Long hostId);
    Long getGroupScoreForSession(Long groupId, Long sessionId);
    Long getStudentScoreForSession(Long studentId, Long sessionId);
    List<Student> getPresentStudents(Long sessionId);
    List<Student> getAbsentStudents(Long sessionId);
    Session createSession(CreateSessionDto dto);
    void updateSession(UpdateSessionDto dto);
}
