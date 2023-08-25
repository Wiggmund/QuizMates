package org.example.quizMates.dto.studentsPairs;

import org.example.quizMates.dto.pair.CreatePairDto;

import java.util.List;

public class PairResponse {
    private List<CreatePairDto> currentPairList;
    private List<Integer> unpairedStudentsList;

    public PairResponse(List<CreatePairDto> currentPairList, List<Integer> absentStudentsList) {
        this.currentPairList = currentPairList;
        this.unpairedStudentsList = absentStudentsList;
    }

    public List<CreatePairDto> getCurrentPairList() {
        return currentPairList;
    }

    public List<Integer> getAbsentStudentsList() {
        return unpairedStudentsList;
    }
}
