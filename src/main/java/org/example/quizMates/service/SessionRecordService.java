package org.example.quizMates.service;


import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.model.SessionRecord;

public interface SessionRecordService extends CrudService<SessionRecord, Long>{
    void createSessionRecord(CreateSessionRecordDto dto);
    void updateSessionRecord(UpdateSessionRecordDto dto);
}
