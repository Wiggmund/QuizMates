package org.example.quizMates.service.impl;

import org.example.quizMates.dto.http.GeneratePairsRequestDto;
import org.example.quizMates.dto.http.GeneratePairsResponseDto;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.model.*;
import org.example.quizMates.service.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentsPairsServiceImpl implements StudentsPairsService {
    private final SessionService sessionService;
    private final SessionRecordService sessionRecordService;
    private final PairService pairService;
    private final StudentService studentService;
    private final GroupService groupService;
    private final GroupStudentService groupStudentService;

    private StudentsPairsServiceImpl() {
        this.sessionService = SessionServiceImpl.getInstance();
        this.sessionRecordService = SessionRecordServiceImpl.getInstance();
        this.pairService = PairServiceImpl.getInstance();
        this.studentService = StudentServiceImpl.getInstance();
        this.groupService = GroupServiceImpl.gteInstance();
        this.groupStudentService = GroupStudentServiceImpl.getInstance();
    }

    private static class StudentPairServiceImplSingleton {
        private static final StudentsPairsService INSTANCE = new StudentsPairsServiceImpl();
    }

    public static StudentsPairsService getInstance() {
        return StudentPairServiceImplSingleton.INSTANCE;
    }

    @Override
    public GeneratePairsResponseDto generatePairs(GeneratePairsRequestDto payload) {
        List<Long> groupsIds = payload.getGroupsIds();
        List<Long> absentStudents = payload.getAbsentStudents();

        List<Student> studentsFromGroups = getStudentsFromGroups(groupsIds);
        List<Student> presentStudents = getPresentStudents(studentsFromGroups, absentStudents);
        List<Pair> previousPairs = getPreviousPairs();

        return previousPairs.isEmpty()
                ? generatePairs(presentStudents)
                : generatePairsBasedOnPrevious(presentStudents, previousPairs);
    }

    private GeneratePairsResponseDto generatePairsBasedOnPrevious(List<Student> presentStudents, List<Pair> previousPairs) {
        Map<Long, Map<Long, Student>> possibleOpponentsForGroups = getGroupsWithPossibleOpponents(presentStudents);
        Helper helper = new Helper();

        for(Student studentA : presentStudents) {
            Long studentAId = studentA.getId();
            if (helper.isStudentTaken.test(studentAId)) continue;

            Long studentBId = null;
            Map<Long, Student> possibleOpponents = possibleOpponentsForGroups.get(studentA.getGroupId());


            for (Long opponentId : possibleOpponents.keySet()) {
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
                possibleOpponents.remove(studentBId);
                helper.addToTakenStudentsAndToGeneratedPairDtos.accept(studentAId, studentBId);
                continue;
            }

            helper.addToTakenStudents.accept(studentAId);
            helper.unpairedStudents.add(studentAId);
        }

        return GeneratePairsResponseDto.builder()
                .pairs(getPairOrCreateIfNotExist(helper.generatedPairsDtos))
                .unpairedStudentsIds(helper.unpairedStudents)
                .build();
    }

    private GeneratePairsResponseDto generatePairs(List<Student> presentStudents) {
        Map<Long, Map<Long, Student>> possibleOpponentsForGroups = getGroupsWithPossibleOpponents(presentStudents);
        Helper helper = new Helper();

        for(Student studentA : presentStudents) {
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

        return GeneratePairsResponseDto.builder()
                .pairs(getPairOrCreateIfNotExist(helper.generatedPairsDtos))
                .unpairedStudentsIds(helper.unpairedStudents)
                .build();
    }

    private Map<Long, Map<Long, Student>> getGroupsWithPossibleOpponents(List<Student> presentStudents) {
        Map<Long, List<Student>> presentStudentsByGroup = presentStudents.stream()
                .collect(Collectors.groupingBy(Student::getGroupId));

        return presentStudents.stream()
                .collect(Collectors.toMap(
                        Student::getGroupId,
                        student -> presentStudentsByGroup.keySet().stream()
                                .filter(groupId -> !Objects.equals(groupId, student.getGroupId()))
                                .flatMap(groupId -> presentStudentsByGroup.get(groupId).stream())
                                .collect(Collectors.toMap(Student::getId, Function.identity()))
                ));
    }

    private List<Pair> getPairOrCreateIfNotExist(List<CreatePairDto> dtos) {
        List<Pair> fetchedPairEntities = pairService.findPairsByStudents(dtos);
        List<CreatePairDto> notExistedPairsDtos = dtos.stream()
                .filter(dto -> fetchedPairEntities.stream().
                        noneMatch(pair -> pair.isPairConsistOfGivenStudents(dto.getStudentA(), dto.getStudentB())))
                .toList();

        if (notExistedPairsDtos.isEmpty()) {
            return fetchedPairEntities;
        }

        for (CreatePairDto dto : notExistedPairsDtos) {
            pairService.createPair(dto);
        }

        return pairService.findPairsByStudents(dtos);
    }

    private List<Pair> getPreviousPairs() {
        Optional<Session> candidate = sessionService.getLastSession();

        if (candidate.isEmpty()) {
            return new ArrayList<>();
        }

        Session lastSession = candidate.get();
        List<Long> pairsIds = sessionRecordService.findBySessionId(lastSession.getId()).stream()
                .map(SessionRecord::getPairId)
                .toList();

        return pairService.findPairsByIds(pairsIds);
    }

    private List<Student> getStudentsFromGroups(List<Long> groupsIds) {
        return groupsIds.stream()
                .flatMap(groupId -> groupStudentService.getAllGroupStudents(groupId).stream())
                .toList();
    }

    private List<Student> getPresentStudents(List<Student> allStudents, List<Long> absentStudents) {
        return allStudents.stream()
                .filter(st -> !absentStudents.contains(st.getId()))
                .toList();
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
