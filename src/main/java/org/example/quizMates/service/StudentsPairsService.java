package org.example.quizMates.service;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.studentsPairs.PairResponse;
import org.example.quizMates.model.Pair;

import java.util.List;

public interface StudentsPairsService {
    List<CreatePairDto> generatePairs(List<Long> listGroups, List<Long> listAbsentStudents);
    PairResponse getPairsAndUnpairesStudentLists(List<Long> listGroups, List<Long> listAbsentStudents);
}
