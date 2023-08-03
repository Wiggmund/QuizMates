package org.example.quizMates.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm {
    static List<Integer> studentGpoupA;
    static List<Integer> studentGpoupB;
    static List<Pair> previousStudentPairs;
    static List<Pair> currentStudentPairs = new ArrayList<>();

    public static List<Pair> chooseRandomPair(List<Integer> groupA, List<Integer> groupB) {
        if (groupA.isEmpty() || groupB.isEmpty()) {
            return null;
        }
        Collections.shuffle(groupA);
        Collections.shuffle(groupB);
        List<Integer> currStudB = new ArrayList<>();

        for (int i = 0; i < Math.min(groupA.size(), groupB.size()); i++) {
            Integer stA = groupA.get(i);
            Integer stB = groupB.stream()
                    .filter(stb -> !stb.equals(getStudentsPreviousPair(stA, previousStudentPairs)))
                    .filter(stb -> !currStudB.contains(stb)) // убираем если участвовал в выбоорке
                    .findAny().orElse(-1);
            currStudB.add(stB);
            currentStudentPairs.add(new Pair(stA, stB));
        }
        return currentStudentPairs;
        // нужно будет дописать логику перехода currentStudentPairs -> previousStudentPairs после окончания работы
    }

    public static Integer getStudentsPreviousPair(Integer student, List<Pair> previousStudentPairs) {
        return previousStudentPairs.stream()
                .filter(candidate ->
                        candidate.getStudentOne().equals(student) || candidate.getStudentTwo().equals(student))
                .map(candidate -> candidate.getStudentOne().equals(student) ? candidate.getStudentTwo() : candidate.getStudentOne())
                .findFirst()
                .orElse(-1);
    }

    public static List<Integer> getStudentListWithoutAbsent(List<Integer> studentGroup, List<Integer> absentStudentList){
        return studentGroup.stream()
                .filter(student -> !absentStudentList.contains(student))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Integer> getUnpairedStudents(List<Integer> studentGpoupA, List<Integer> studentGpoupB,
                                                    List<Pair> currentStudentPairs, List<Integer> absentStudentList){
        List<Integer> allStudents = Stream.concat(studentGpoupA.stream(), studentGpoupB.stream()).toList();
        List<Integer> allStudentsWithoutAbsent = getStudentListWithoutAbsent(allStudents, absentStudentList);
        List<Integer> studentsFromPairA = currentStudentPairs.stream()
                .map(Pair::getStudentOne)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Integer> studentsFromPairB = currentStudentPairs.stream()
                .map(Pair::getStudentTwo)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Integer> allPairedStudents = Stream.concat(studentsFromPairA.stream(), studentsFromPairB.stream()).toList();
        return allStudentsWithoutAbsent.stream()
                .filter(student -> !allPairedStudents.contains(student))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        studentGpoupA = new ArrayList<>(List.of(1,3,5));
        studentGpoupB = new ArrayList<>(List.of(2,4,6));
        List<Integer> absentStudents = new ArrayList<>(List.of(3));
        previousStudentPairs = List.of(
                new Pair(1, 2),
                new Pair(3, 4),
                new Pair(5,6));
        //System.out.println(algorithm.getStudentsPreviousPair(6, previousStudentPairs));
        System.out.println(Algorithm.chooseRandomPair(Algorithm.getStudentListWithoutAbsent(studentGpoupA, absentStudents),
                Algorithm.getStudentListWithoutAbsent(studentGpoupB, absentStudents)));
        //System.out.println(Algorithm.getStudentListWithoutAbsent(studentGpoupA, absentStudents));
        System.out.println(Algorithm.getUnpairedStudents(studentGpoupA,studentGpoupB, currentStudentPairs, absentStudents));
    }
}
class Pair {
    private final Integer studentOne;
    private final Integer studentTwo;

    @Override
    public String toString() {
        return "Pair{" +
                "studentOne=" + studentOne +
                ", studentTwo=" + studentTwo +
                '}';
    }

    public Pair(Integer s1, Integer s2) {
        this.studentOne = s1;
        this.studentTwo = s2;
    }

    public Integer getStudentTwo() {
        return studentTwo;
    }

    public Integer getStudentOne() {
        return studentOne;
    }
}
record ClientRequest(
        List<Long> groups,
        List<Long> absentStudents
){}

record Response(
        List<Pair> pairs,
        List<Long> unpairedStudents
){}
