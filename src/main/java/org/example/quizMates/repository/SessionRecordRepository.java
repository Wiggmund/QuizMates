package org.example.quizMates.repository;

import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.model.SessionRecord;

import java.util.Optional;

public interface SessionRecordRepository extends CrudRepository<SessionRecord, Long>{
    Optional<SessionRecord> findById(Long sessionId, Long pairId, Long studentId, Long hostId);
    void createSessionRecord(CreateSessionRecordDto dto);
    void updateSessionRecord(UpdateSessionRecordDto dto);
    void deleteById(Long sessionId, Long pairId, Long studentId, Long hostId)
}
