package org.example.quizMates.utils;

import org.example.quizMates.dto.pair.CreatePairDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm {
    static List<Long> studentGpoupA;
    static List<Long> studentGpoupB;
    static List<CreatePairDto> previousStudentPairs;
    static List<CreatePairDto> currentStudentPairs = new ArrayList<>();

    public static List<CreatePairDto> chooseRandomPair(List<Long> groupA, List<Long> groupB) {
        if (groupA.isEmpty() || groupB.isEmpty()) {
            return null;
        }
        Collections.shuffle(groupA);
        Collections.shuffle(groupB);
        List<Long> currStudB = new ArrayList<>();

        for (int i = 0; i < Math.min(groupA.size(), groupB.size()); i++) {
            Long stA = groupA.get(i);
            Long stB = groupB.stream()
                    .filter(stb -> !stb.equals(getStudentsPreviousPair(stA, previousStudentPairs)))
                    .filter(stb -> !currStudB.contains(stb)) // убираем если участвовал в выбоорке
                    .findAny().orElse(-1L);
            currStudB.add(stB);
            currentStudentPairs.add(new CreatePairDto(stA, stB));
        }
        return currentStudentPairs;
        // нужно будет дописать логику перехода currentStudentPairs -> previousStudentPairs после окончания работы
    }

    public static Long getStudentsPreviousPair(Long student, List<CreatePairDto> previousStudentPairs) {
        return previousStudentPairs.stream()
                .filter(candidate ->
                        candidate.getStudentA().equals(student) || candidate.getStudentB().equals(student))
                .map(candidate -> candidate.getStudentA().equals(student) ? candidate.getStudentB() : candidate.getStudentA())
                .findFirst()
                .orElse(-1L);
    }

    public static List<Long> getStudentListWithoutAbsent(List<Long> studentGroup, List<Long> absentStudentList){
        return studentGroup.stream()
                .filter(student -> !absentStudentList.contains(student))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Long> getUnpairedStudents(List<Long> studentGpoupA, List<Long> studentGpoupB,
                                                    List<CreatePairDto> currentStudentPairs, List<Long> absentStudentList){
        List<Long> allStudents = Stream.concat(studentGpoupA.stream(), studentGpoupB.stream()).toList();
        List<Long> allStudentsWithoutAbsent = getStudentListWithoutAbsent(allStudents, absentStudentList);
        List<Long> studentsFromPairA = currentStudentPairs.stream()
                .map(CreatePairDto::getStudentA)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Long> studentsFromPairB = currentStudentPairs.stream()
                .map(CreatePairDto::getStudentB)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Long> allPairedStudents = Stream.concat(studentsFromPairA.stream(), studentsFromPairB.stream()).toList();
        return allStudentsWithoutAbsent.stream()
                .filter(student -> !allPairedStudents.contains(student))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        studentGpoupA = new ArrayList<>(List.of(1L,3L,5L));
        studentGpoupB = new ArrayList<>(List.of(2L,4L,6L));
        List<Long> absentStudents = new ArrayList<>(List.of(3L));
        previousStudentPairs = List.of(
                new CreatePairDto(1L, 2L),
                new CreatePairDto(3L, 4L),
                new CreatePairDto(5L,6L));
        //System.out.println(algorithm.getStudentsPreviousPair(6, previousStudentPairs));
        System.out.println(Algorithm.chooseRandomPair(Algorithm.getStudentListWithoutAbsent(studentGpoupA, absentStudents),
                Algorithm.getStudentListWithoutAbsent(studentGpoupB, absentStudents)));
        //System.out.println(Algorithm.getStudentListWithoutAbsent(studentGpoupA, absentStudents));
        System.out.println(Algorithm.getUnpairedStudents(studentGpoupA,studentGpoupB, currentStudentPairs, absentStudents));
    }
}

record ClientRequest(
        List<Long> groups,
        List<Long> absentStudents
){}

record Response(
        List<CreatePairDto> pairs,
        List<Long> unpairedStudents
){}
