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
        return null;
    }
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
                    .filter(stb -> !stb.equals(getStudentsPreviousPair(stA, currentStudentPairs)))
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


}
