package org.example.quizMates.utils;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.model.Pair;
import org.example.quizMates.model.Session;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.model.Student;
import org.example.quizMates.service.GroupStudentService;
import org.example.quizMates.service.PairService;
import org.example.quizMates.service.SessionRecordService;
import org.example.quizMates.service.SessionService;
import org.example.quizMates.service.impl.GroupStudentServiceImpl;
import org.example.quizMates.service.impl.PairServiceImpl;
import org.example.quizMates.service.impl.SessionRecordServiceImpl;
import org.example.quizMates.service.impl.SessionServiceImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneratePairsHelper {
    private final SessionService sessionService;
    private final SessionRecordService sessionRecordService;
    private final PairService pairService;
    private final GroupStudentService groupStudentService;

    private GeneratePairsHelper() {
        this.sessionService = SessionServiceImpl.getInstance();
        this.sessionRecordService = SessionRecordServiceImpl.getInstance();
        this.pairService = PairServiceImpl.getInstance();
        this.groupStudentService = GroupStudentServiceImpl.getInstance();
    }

    private GeneratePairsHelper(SessionService sessionService,
                                     SessionRecordService sessionRecordService,
                                     PairService pairService,
                                     GroupStudentService groupStudentService) {
        this.sessionService = sessionService;
        this.sessionRecordService = sessionRecordService;
        this.pairService = pairService;
        this.groupStudentService = groupStudentService;
    }

    private static class GeneratePairsHelperSingleton {
        private static final GeneratePairsHelper INSTANCE = new GeneratePairsHelper();
    }

    public static GeneratePairsHelper getInstance() {
        return GeneratePairsHelperSingleton.INSTANCE;
    }

    public Map<Long, Map<Long, Student>> getGroupsWithPossibleOpponents(List<Student> presentStudents) {
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

    public List<Pair> getPairOrCreateIfNotExist(List<CreatePairDto> dtos) {
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

    public List<Pair> getPreviousPairs() {
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

    public List<Student> getStudentsFromGroups(List<Long> groupsIds) {
        return groupsIds.stream()
                .flatMap(groupId -> groupStudentService.getAllGroupStudents(groupId).stream())
                .toList();
    }

    public List<Student> getPresentStudents(List<Student> allStudents, List<Long> absentStudents) {
        return allStudents.stream()
                .filter(st -> !absentStudents.contains(st.getId()))
                .toList();
    }
}
