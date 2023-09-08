package org.example.quizMates.service.impl;

import org.example.quizMates.dto.http.GeneratePairsRequestDto;
import org.example.quizMates.dto.http.GeneratePairsResponseDto;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.model.*;
import org.example.quizMates.service.*;
import org.example.quizMates.utils.GeneratePairsHelper;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentsPairsServiceImpl implements StudentsPairsService {
    private final GeneratePairsHelper generateUtil;

    private StudentsPairsServiceImpl() {
        this.generateUtil = GeneratePairsHelper.getInstance();
    }

    private StudentsPairsServiceImpl(GeneratePairsHelper generateUtil) {
        this.generateUtil = generateUtil;
    }

    private static class StudentPairServiceImplSingleton {
        private static final StudentsPairsService INSTANCE = new StudentsPairsServiceImpl();
    }

    public static StudentsPairsService getInstance() {
        return StudentPairServiceImplSingleton.INSTANCE;
    }

    @Override
    public synchronized GeneratePairsResponseDto generatePairs(GeneratePairsRequestDto payload) {
        List<Long> groupsIds = payload.getGroupsIds();
        List<Long> absentStudents = payload.getAbsentStudents();
        boolean byAllStudents = payload.isByAllStudents();

        List<Student> studentsFromGroups = generateUtil.getStudentsFromGroups(groupsIds);
        List<Student> presentStudents = generateUtil.getPresentStudents(studentsFromGroups, absentStudents);
        List<Pair> previousPairs = generateUtil.getPreviousPairs();

        return byAllStudents
                ? generatePairsByAllStudents(presentStudents)
                : previousPairs.isEmpty()
                ? generatePairs(presentStudents)
                : generatePairsBasedOnPrevious(presentStudents, previousPairs);
    }

    private GeneratePairsResponseDto generatePairsBasedOnPrevious(List<Student> presentStudents, List<Pair> previousPairs) {
        Map<Long, Map<Long, Student>> possibleOpponentsForGroups = generateUtil.getGroupsWithPossibleOpponents(presentStudents);
        Helper helper = new Helper();

        for (Student studentA : presentStudents) {
            Long studentAId = studentA.getId();
            if (helper.isStudentTaken.test(studentAId)) continue;

            Long studentBId = null;
            Map<Long, Student> possibleOpponents = possibleOpponentsForGroups.get(studentA.getGroupId());


            for (Long opponentId : possibleOpponents.keySet()) {
                if (helper.isStudentTaken.test(opponentId)) continue;

                boolean theSameAsPreviousPair = previousPairs.stream().anyMatch(pair ->
                        pair.isPairConsistOfGivenStudents(studentAId, opponentId));

                if (!theSameAsPreviousPair) {
                    studentBId = opponentId;
                    break;
                }
            }

            if (studentBId != null) {
                possibleOpponents.remove(studentBId);
                helper.addToTakenStudentsAndToGeneratedPairDtos.accept(studentAId, studentBId);
                continue;
            }

            if (possibleOpponents.size() == 1) {
                studentBId = possibleOpponents.keySet().iterator().next();

                if (!helper.isStudentTaken.test(studentBId)) {
                    possibleOpponents.remove(studentBId);
                    helper.addToTakenStudentsAndToGeneratedPairDtos.accept(studentAId, studentBId);
                    continue;
                }
            }

            helper.addToTakenStudents.accept(studentAId);
            helper.unpairedStudents.add(studentAId);
        }

        List<Pair> generatedAndFetchedPairs = helper.generatedPairsDtos.isEmpty()
                ? new ArrayList<>()
                : generateUtil.getPairOrCreateIfNotExist(helper.generatedPairsDtos);

        return GeneratePairsResponseDto.builder()
                .pairs(generatedAndFetchedPairs)
                .unpairedStudentsIds(helper.unpairedStudents)
                .build();
    }

    private GeneratePairsResponseDto generatePairs(List<Student> presentStudents) {
        Map<Long, Map<Long, Student>> possibleOpponentsForGroups = generateUtil.getGroupsWithPossibleOpponents(presentStudents);
        Helper helper = new Helper();

        for (Student studentA : presentStudents) {
            Long studentAId = studentA.getId();
            if (helper.isStudentTaken.test(studentAId)) continue;

            Long studentBId = null;
            Map<Long, Student> possibleOpponents = possibleOpponentsForGroups.get(studentA.getGroupId());
            Iterator<Long> iterator = possibleOpponents.keySet().iterator();

            if (!iterator.hasNext()) {
                helper.unpairedStudents.add(studentAId);
                helper.addToTakenStudents.accept(studentAId);
                continue;
            }

            studentBId = iterator.next();
            iterator.remove();
            helper.addToTakenStudentsAndToGeneratedPairDtos.accept(studentAId, studentBId);
        }

        List<Pair> generatedAndFetchedPairs = helper.generatedPairsDtos.isEmpty()
                ? new ArrayList<>()
                : generateUtil.getPairOrCreateIfNotExist(helper.generatedPairsDtos);

        return GeneratePairsResponseDto.builder()
                .pairs(generatedAndFetchedPairs)
                .unpairedStudentsIds(helper.unpairedStudents)
                .build();
    }

    private GeneratePairsResponseDto generatePairsByAllStudents(List<Student> presentStudents) {
        Helper helper = new Helper();
        LinkedList<Long> possibleOpponents = presentStudents.stream()
                .map(Student::getId)
                .collect(Collectors.toCollection(LinkedList::new));

        Collections.shuffle(possibleOpponents);


        for(Student studentA : presentStudents) {
            Long studentAId = studentA.getId();
            if (helper.isStudentTaken.test(studentAId)) continue;

            boolean goToNextStudent = false;
            do {
                Long opponentCandidate = possibleOpponents.poll();

                if (opponentCandidate == null) {
                    helper.unpairedStudents.add(studentAId);
                    helper.addToTakenStudents.accept(studentAId);
                    goToNextStudent = true;
                    continue;
                }

                if (opponentCandidate.equals(studentAId)) continue;

                if (helper.isStudentTaken.test(opponentCandidate)) continue;

                helper.addToTakenStudentsAndToGeneratedPairDtos.accept(studentAId, opponentCandidate);
                goToNextStudent = true;

            } while(!goToNextStudent);
        }

        List<Pair> generatedAndFetchedPairs = helper.generatedPairsDtos.isEmpty()
                ? new ArrayList<>()
                : generateUtil.getPairOrCreateIfNotExist(helper.generatedPairsDtos);

        return GeneratePairsResponseDto.builder()
                .pairs(generatedAndFetchedPairs)
                .unpairedStudentsIds(helper.unpairedStudents)
                .build();
    }

    private static class Helper {
        List<CreatePairDto> generatedPairsDtos = new ArrayList<>();
        List<Long> unpairedStudents = new ArrayList<>();
        Map<Long, Long> takenStudents = new HashMap<>();

        Predicate<Long> isStudentTaken = takenStudents::containsKey;
        Consumer<Long> addToTakenStudents = (id) -> takenStudents.put(id, id);
        BiConsumer<Long, Long> addToGeneratedPairDtos = (studentA, studentB) -> generatedPairsDtos.add(
                CreatePairDto.builder()
                        .studentA(studentA)
                        .studentB(studentB)
                        .build()
        );

        BiConsumer<Long, Long> addToTakenStudentsAndToGeneratedPairDtos = (studentA, studentB) -> {
            addToTakenStudents.accept(studentA);
            addToTakenStudents.accept(studentB);
            addToGeneratedPairDtos.accept(studentA, studentB);
        };
    }
}
