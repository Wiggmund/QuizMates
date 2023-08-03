package org.example.quizMates.service.impl;

import org.example.quizMates.model.Pair;
import org.example.quizMates.model.Session;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.service.SessionRecordService;
import org.example.quizMates.service.SessionService;
import org.example.quizMates.service.StudentsPairsService;

import java.util.List;

public class StudentsPairsServiceImpl implements StudentsPairsService {
    private final SessionService sessionService;
    private final SessionRecordService sessionRecordService;
    private StudentsPairsServiceImpl() {
        this.sessionService = SessionServiceImpl.getInstance();
        this.sessionRecordService = SessionRecordServiceImpl.getInstance();
    }

    private static class StudentPairServiceImplSingleton {
        private static final StudentsPairsService INSTANCE = new StudentsPairsServiceImpl();
    }

    public static StudentsPairsService getInstance() {
        return StudentPairServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Pair> generatePairs() {
        Session session = sessionService.getLastSession().get();
        List<SessionRecord> fetchedRecords = sessionRecordService.findBySessionId(session.getId());
        List<Long> pairIds = fetchedRecords.stream().map(SessionRecord::getPairId).toList();

        return null;
    }
}
