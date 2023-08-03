package org.example.quizMates.service.impl;

import org.example.quizMates.model.Pair;
import org.example.quizMates.service.StudentsPairsService;

import java.util.List;

public class StudentsPairsServiceImpl implements StudentsPairsService {
    private StudentsPairsServiceImpl() {}

    private static class StudentPairServiceImplSingleton {
        private static final StudentsPairsService INSTANCE = new StudentsPairsServiceImpl();
    }

    public static StudentsPairsService getInstance() {
        return StudentPairServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Pair> generatePairs() {
        return null;
    }
}
