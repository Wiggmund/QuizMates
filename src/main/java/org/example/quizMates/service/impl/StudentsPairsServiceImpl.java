package org.example.quizMates.service.impl;

import org.example.quizMates.model.Pair;
import org.example.quizMates.model.Session;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.model.Student;
import org.example.quizMates.service.*;

import java.util.List;
import java.util.stream.Stream;

public class StudentsPairsServiceImpl implements StudentsPairsService {
    private final SessionService sessionService;
    private final SessionRecordService sessionRecordService;
    private final PairService pairService;
    private final StudentService studentService;
    private StudentsPairsServiceImpl() {
        this.sessionService = SessionServiceImpl.getInstance();
        this.sessionRecordService = SessionRecordServiceImpl.getInstance();
        this.pairService = PairServiceImpl.getInstance();
        this.studentService = StudentServiceImpl.getInstance();
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
        List<Pair> pairsList = pairService.findPairsByIds(pairIds);
        List<Long> studentsIds = pairsList.stream()
                .flatMap(pair -> Stream.of(pair.getStudentA(), pair.getStudentB()))
                .toList();
        List<Student> studentsList = studentService.findStudentsByIds(studentsIds);

        return null;
    }
}
