package org.example.quizMates.service.impl;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.model.Pair;
import org.example.quizMates.model.Session;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.model.Student;
import org.example.quizMates.service.*;

import java.util.*;
import java.util.stream.Collectors;
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
        final Long EMPTY = -1L;
        Session session = sessionService.getLastSession().get();
        List<SessionRecord> fetchedRecordsForLastSession = sessionRecordService.findBySessionId(session.getId());
        List<Long> lastSessionPairIds = fetchedRecordsForLastSession.stream().map(SessionRecord::getPairId).toList();
        List<Pair> lastSessionPairsList = pairService.findPairsByIds(lastSessionPairIds);


        /*-------------------*/
        List<Student> allStudents = studentService.findAll();
        Map<Long, List<Student>> allStudentsByGroup = allStudents.stream()
                .collect(Collectors.groupingBy(Student::getGroupId));
        /*-------------------*/


        /*-------------------*/
        Map<Long, Long> allStudentsWithPairLastSession = new HashMap<>();
        Map<Long, Long> aStudents = lastSessionPairsList.stream().collect(Collectors.toMap(Pair::getStudentA, Pair::getStudentB));
        Map<Long, Long> bStudents = lastSessionPairsList.stream().collect(Collectors.toMap(Pair::getStudentB, Pair::getStudentA));

        aStudents.forEach((key, value) -> allStudentsWithPairLastSession.merge(key, value, (a, b) -> {
            throw new IllegalArgumentException("Duplicated key");}));
        bStudents.forEach((key, value) -> allStudentsWithPairLastSession.merge(key, value, (a, b) -> {
            throw new IllegalArgumentException("Duplicated key");}));
        /*-------------------*/


        Map<Long, List<Long>> allStudentsWithPossiblePair = new HashMap<>();
        for (Student currentStudent : allStudents) {
            List<Long> possiblePairIds = allStudentsByGroup.keySet().stream()
                    .filter(groupId -> !Objects.equals(groupId, currentStudent.getGroupId()))
                    .toList();

            allStudentsWithPossiblePair.put(currentStudent.getId(), possiblePairIds);
        }


        /*-------------------*/
        List<CreatePairDto> resultDtos = new ArrayList<>();
        Map<Long, Long> takenStudents = new HashMap<>();

        allStudentsWithPossiblePair.forEach((currentStudent, possiblePairsIds) -> {
            boolean doCurrentStudentTaken = Objects.equals(takenStudents.getOrDefault(currentStudent, EMPTY), EMPTY);
            if (doCurrentStudentTaken) return;

            Long studentA = currentStudent;
            Long studentB;

            Long lastSessionPair = allStudentsWithPairLastSession.get(currentStudent);
            for (int i = 0; i < possiblePairsIds.size(); i++) {
                Long possiblePair = possiblePairsIds.get(i);
                boolean doPossiblePairTheSameAsLastSession = Objects.equals(possiblePair, lastSessionPair);
                boolean doPossiblePairTaken = Objects.equals(takenStudents.getOrDefault(possiblePair, EMPTY), EMPTY);

                if (!doPossiblePairTheSameAsLastSession && !doPossiblePairTaken) {
                    studentB = possiblePair;

                    resultDtos.add(CreatePairDto.builder()
                            .studentA(studentA)
                            .studentB(studentB)
                            .build());

                    takenStudents.put(studentA, studentA);
                    takenStudents.put(studentB, studentB);
                    break;
                }
            }
        });
        /*-------------------*/



        /*-------------------*/
        List<Pair> allExistedPairs = pairService.findAll();
        Map<Long, Long> asKeyStudentA = allExistedPairs.stream()
                .collect(Collectors.toMap(Pair::getStudentA, Pair::getStudentB));
        Map<Long, Long> asKeyStudentB = allExistedPairs.stream()
                .collect(Collectors.toMap(Pair::getStudentB, Pair::getStudentA));

        List<CreatePairDto> notExistedPairs = resultDtos.stream().filter((dto) -> {
            /*
             * A) Pair(studentA = 1, studentB = 2)
             * B) Pair(studentA = 2, studentB = 1)
             *
             * Option A is obviously the same as option B. So we have to check both to filter
             * */

            /*
             * Option A
             * */
            boolean doStudentAPresentAsStudentA = Objects.equals(asKeyStudentA.getOrDefault(dto.getStudentA(), EMPTY), EMPTY);
            boolean doStudentBPresentAsStudentB = Objects.equals(asKeyStudentB.getOrDefault(dto.getStudentB(), EMPTY), EMPTY);
            if (doStudentAPresentAsStudentA && doStudentBPresentAsStudentB) return true;

            /*
             * Option B
             * */
            boolean doStudentBPresentAsStudentA = Objects.equals(asKeyStudentA.getOrDefault(dto.getStudentB(), EMPTY), EMPTY);
            boolean doStudentAPresentAsStudentB = Objects.equals(asKeyStudentB.getOrDefault(dto.getStudentA(), EMPTY), EMPTY);
            if (doStudentBPresentAsStudentA && doStudentAPresentAsStudentB) return true;

            return false;
        }).toList();

        if(!notExistedPairs.isEmpty()) {
            for (CreatePairDto dto : notExistedPairs) {
                pairService.createPair(dto);
            }
        }
        /*-------------------*/

        return new ArrayList<>();
    }
}
