package org.example.quizMates.repository;

import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.model.SessionRecord;

import java.util.List;
import java.util.Optional;

public interface SessionRecordRepository extends CrudRepository<SessionRecord, Long>{
    Optional<SessionRecord> findById(Long id);
    List<SessionRecord> findByStudentId(Long id);
    List<SessionRecord> findBySessionId(Long id);
    List<SessionRecord> findByStudentIdAndSessionId(Long studentId, Long sessionId);
    void createSessionRecord(CreateSessionRecordDto dto);
    void updateSessionRecord(UpdateSessionRecordDto dto);
    void deleteById(Long id);
}
