package org.example.quizMates.service.impl;

import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.repository.SessionRecordRepository;
import org.example.quizMates.repository.impl.SessionRecordRepositoryImpl;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.SessionRecordService;

import java.util.List;

public class SessionRecordServiceImpl implements SessionRecordService {
    private final SessionRecordRepository sessionRecordRepository;
    private final DuplicationService duplicationService;
    private final static String SESSION_RECORD_NOT_FOUND = "Session record with id %s not found";
    private final static String SESSION_RECORD_DUPLICATE = "Session record with title %s already exists";

    private SessionRecordServiceImpl() {
        this.sessionRecordRepository = SessionRecordRepositoryImpl.getInstance();
        this.duplicationService = DuplicationServiceImpl.getInstance();
    }

    private static class SessionRecordServiceSingleton {
        private static final SessionRecordService INSTANCE = new SessionRecordServiceImpl();
    }

    public static SessionRecordService getInstance() {
        return SessionRecordServiceImpl.SessionRecordServiceSingleton.INSTANCE;
    }
    @Override
    public SessionRecord findById(Long id) {
        return sessionRecordRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SESSION_RECORD_NOT_FOUND, id)));
    }

    @Override
    public List<SessionRecord> findAll() {
        return sessionRecordRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        sessionRecordRepository.deleteById(id);
    }

    @Override
    public void createSessionRecord(CreateSessionRecordDto dto) {
        sessionRecordRepository.createSessionRecord(dto);
    }

    @Override
    public void updateSessionRecord(UpdateSessionRecordDto dto) {

    }
}
