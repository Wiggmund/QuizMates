package org.example.quizMates.service;


import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.model.Host;
import org.example.quizMates.model.SessionRecord;

import java.util.List;
import java.util.Optional;

public interface SessionRecordService extends CrudService<SessionRecord, Long>{
    List<SessionRecord> findByStudentId(Long id);
    List<SessionRecord> findBySessionId(Long id);
    void createSessionRecord(CreateSessionRecordDto dto);
    void updateSessionRecord(UpdateSessionRecordDto dto);
    Host findByIdAndGetHostId(Long id);
}
