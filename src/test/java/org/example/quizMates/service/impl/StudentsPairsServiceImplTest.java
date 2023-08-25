package org.example.quizMates.service.impl;

import jdk.jfr.Description;
import org.assertj.core.api.Assertions;
import org.example.quizMates.dto.http.GeneratePairsRequestDto;
import org.example.quizMates.dto.http.GeneratePairsResponseDto;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.model.Group;
import org.example.quizMates.model.Pair;
import org.example.quizMates.model.Student;
import org.example.quizMates.utils.GeneratePairsHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsPairsServiceImplTest {
    @Mock
    private GeneratePairsHelper generatePairsHelper;
    @Mock
    private GeneratePairsRequestDto generatePairsRequestDto;

    @InjectMocks StudentsPairsServiceImpl studentsPairsService;

    private static List<Student> studentsGroup1 = new ArrayList<>();
    private static List<Student> studentsGroup2 = new ArrayList<>();
    private static List<Student> studentsGroup3 = new ArrayList<>();
    private static List<Student> studentsGroup1AndGroup2 = new ArrayList<>();
    private static List<Student> studentsGroup1AndGroup2PlusUnpaired = new ArrayList<>();
    private static List<Student> absentStudentsGroup1 = new ArrayList<>();
    private static List<Student> absentStudentsGroup2 = new ArrayList<>();
    private static List<Student> absentStudentsGroup3 = new ArrayList<>();
    private static List<Student> allAbsentStudents = new ArrayList<>();
    private static List<Long> allAbsentStudentsIds = new ArrayList<>();
    private static List<Long> absentStudentsIdsGroup1AndGroup2 = new ArrayList<>();
    private static List<Student> unpairedStudents = new ArrayList<>();
    private static List<Long> unpairedStudentsIds = new ArrayList<>();
    private static List<Student> allStudentsGroup1AndGroup2 = new ArrayList<>();
    private static List<Student> allStudentsGroup1AndGroup2PlusUnpaired = new ArrayList<>();
    private static List<Student> allStudents = new ArrayList<>();
    private static List<Student> allPresentStudents = new ArrayList<>();
    private static List<Group> allGroups = new ArrayList<>();
    private static List<Long> allGroupsIds = new ArrayList<>();
    private static List<Group> group1AndGroup2 = new ArrayList<>();
    private static List<Long> group1AndGroup2Ids = new ArrayList<>();
    private static List<Pair> previousPairsGroup1AndGroup2 = new ArrayList<>();
    private static List<Pair> previousPairs = new ArrayList<>();
    private static List<CreatePairDto> expectedPairsGroup1AndGroup2 = new ArrayList<>();
    private Map<Long, Map<Long, Student>> groupsWithOpponents = new HashMap<>();
    private Map<Long, Map<Long, Student>> groupsWithOpponentsGroup1And2 = new HashMap<>();

    {
        Map<Long, Student> opponentsForGroup1 = new HashMap<>();
        opponentsForGroup1.put(studentsGroup2.get(0).getId(), studentsGroup2.get(0));
        opponentsForGroup1.put(studentsGroup2.get(1).getId(), studentsGroup2.get(1));
        opponentsForGroup1.put(studentsGroup3.get(0).getId(), studentsGroup3.get(0));
        opponentsForGroup1.put(studentsGroup3.get(1).getId(), studentsGroup3.get(1));

        Map<Long, Student> opponentsForGroup2 = new HashMap<>();
        opponentsForGroup2.put(studentsGroup1.get(0).getId(), studentsGroup1.get(0));
        opponentsForGroup2.put(studentsGroup1.get(1).getId(), studentsGroup1.get(1));
        opponentsForGroup2.put(studentsGroup3.get(0).getId(), studentsGroup3.get(0));
        opponentsForGroup2.put(studentsGroup3.get(1).getId(), studentsGroup3.get(1));

        Map<Long, Student> opponentsForGroup3 = new HashMap<>();
        opponentsForGroup3.put(studentsGroup1.get(0).getId(), studentsGroup1.get(0));
        opponentsForGroup3.put(studentsGroup1.get(1).getId(), studentsGroup1.get(1));
        opponentsForGroup3.put(studentsGroup2.get(0).getId(), studentsGroup2.get(0));
        opponentsForGroup3.put(studentsGroup2.get(1).getId(), studentsGroup2.get(1));

        groupsWithOpponents.put(1L, opponentsForGroup1);
        groupsWithOpponents.put(2L, opponentsForGroup2);
        groupsWithOpponents.put(3L, opponentsForGroup3);


        Map<Long, Student> opponentsFromGroup2ForGroup1 = new HashMap<>();
        opponentsFromGroup2ForGroup1.put(studentsGroup2.get(0).getId(), studentsGroup2.get(0));
        opponentsFromGroup2ForGroup1.put(studentsGroup2.get(1).getId(), studentsGroup2.get(1));

        Map<Long, Student> opponentsFromGroup1ForGroup2 = new HashMap<>();
        opponentsFromGroup1ForGroup2.put(studentsGroup1.get(0).getId(), studentsGroup1.get(0));
        opponentsFromGroup1ForGroup2.put(studentsGroup1.get(1).getId(), studentsGroup1.get(1));

        groupsWithOpponentsGroup1And2.put(1L, opponentsFromGroup2ForGroup1);
        groupsWithOpponentsGroup1And2.put(2L, opponentsFromGroup1ForGroup2);
    }

    static {
        Predicate<Long> onlyGroup1AndGroup2 = (id) -> id == 1L || id == 2L;
        Predicate<Group> onlyGroup1AndGroup2Entity = (group) -> onlyGroup1AndGroup2.test(group.getId());


        studentsGroup1.addAll(List.of(
                Student.builder().id(1L).firstName("Sem").lastName("Vinchester").groupId(1L).build(),
                Student.builder().id(2L).firstName("Dean").lastName("Vinchester").groupId(1L).build()
        ));

        studentsGroup2.addAll(List.of(
                Student.builder().id(3L).firstName("Alex").lastName("Murhy").groupId(2L).build(),
                Student.builder().id(4L).firstName("Connor").lastName("Murhy").groupId(2L).build()
        ));

        studentsGroup3.addAll(List.of(
                Student.builder().id(5L).firstName("John").lastName("Connor").groupId(3L).build(),
                Student.builder().id(6L).firstName("Sarah").lastName("Connor").groupId(3L).build()
        ));

        absentStudentsGroup1.add(
                Student.builder().id(7L).firstName("Miki").lastName("Pirson").groupId(1L).build()
        );
        absentStudentsGroup2.add(
                Student.builder().id(8L).firstName("Tommy").lastName("Mikkety").groupId(2L).build()
        );
        absentStudentsGroup3.add(
                Student.builder().id(9L).firstName("Fred").lastName("Luciano").groupId(3L).build()
        );

        unpairedStudents.addAll(List.of(
                Student.builder().id(10L).firstName("Barry").lastName("Hugo").groupId(1L).build(),
                Student.builder().id(11L).firstName("Tod").lastName("Hugo").groupId(1L).build()
        ));
        unpairedStudentsIds.addAll(unpairedStudents.stream().map(Student::getId).toList());

        studentsGroup1AndGroup2.addAll(studentsGroup1);
        studentsGroup1AndGroup2.addAll(studentsGroup2);

        studentsGroup1AndGroup2PlusUnpaired.addAll(studentsGroup1AndGroup2);
        studentsGroup1AndGroup2PlusUnpaired.addAll(unpairedStudents);

        allAbsentStudents.addAll(absentStudentsGroup1);
        allAbsentStudents.addAll(absentStudentsGroup2);
        allAbsentStudents.addAll(absentStudentsGroup3);

        allAbsentStudentsIds.addAll(allAbsentStudents.stream().map(Student::getId).toList());
        absentStudentsIdsGroup1AndGroup2.addAll(
                allAbsentStudents.stream().filter(st -> onlyGroup1AndGroup2.test(st.getGroupId()))
                        .map(Student::getId).toList()
        );

        allStudentsGroup1AndGroup2.addAll(studentsGroup1);
        allStudentsGroup1AndGroup2.addAll(studentsGroup2);
        allStudentsGroup1AndGroup2.addAll(absentStudentsGroup1);
        allStudentsGroup1AndGroup2.addAll(absentStudentsGroup2);

        allStudentsGroup1AndGroup2PlusUnpaired.addAll(unpairedStudents);

        allStudents.addAll(allStudentsGroup1AndGroup2);
        allStudents.addAll(studentsGroup3);
        allStudents.addAll(absentStudentsGroup3);
        allStudents.addAll(unpairedStudents);

        allPresentStudents.addAll(studentsGroup1);
        allPresentStudents.addAll(studentsGroup2);
        allPresentStudents.addAll(studentsGroup3);
        allPresentStudents.addAll(unpairedStudents);


        previousPairsGroup1AndGroup2.addAll(List.of(
                Pair.builder().id(1L).studentA(1L).studentB(3L).build(),
                Pair.builder().id(2L).studentA(2L).studentB(4L).build()
        ));

        previousPairs.addAll(List.of(
                Pair.builder().id(1L).studentA(1L).studentB(3L).build(),
                Pair.builder().id(2L).studentA(4L).studentB(5L).build(),
                Pair.builder().id(3L).studentA(2L).studentB(6L).build()
        ));

        expectedPairsGroup1AndGroup2.addAll(List.of(
                CreatePairDto.builder().studentA(1L).studentB(4L).build(),
                CreatePairDto.builder().studentA(2L).studentB(3L).build()
        ));

        allGroups.addAll(List.of(
                Group.builder().id(1L).name("Group 1").students(studentsGroup1).build(),
                Group.builder().id(2L).name("Group 2").students(studentsGroup2).build(),
                Group.builder().id(3L).name("Group 3").students(studentsGroup3).build()
        ));

        allGroupsIds.addAll(allGroups.stream().map(Group::getId).toList());

        group1AndGroup2.addAll(
                allGroups.stream().filter(onlyGroup1AndGroup2Entity).toList());

        group1AndGroup2Ids.addAll(group1AndGroup2.stream().map(Group::getId).toList());
    }

    @Test
    void mustUseDataFromGeneratePairsDto() {
        //given
        //when
        studentsPairsService.generatePairs(generatePairsRequestDto);

        //then
        verify(generatePairsRequestDto).getAbsentStudents();
        verify(generatePairsRequestDto).getGroupsIds();
    }

    @Test
    @Description("Given: 0 absent students. Then: 2 pairs and 0 unpaired students")
    void noAbsentAndNoUnpaired() {
        //given
        when(generatePairsRequestDto.getGroupsIds()).thenReturn(group1AndGroup2Ids);
        when(generatePairsRequestDto.getAbsentStudents()).thenReturn(new ArrayList<>());

        List<Long> absentStudents = generatePairsRequestDto.getAbsentStudents();

        when(generatePairsHelper.getStudentsFromGroups(group1AndGroup2Ids))
                .thenReturn(studentsGroup1AndGroup2);
        when(generatePairsHelper.getPresentStudents(studentsGroup1AndGroup2, absentStudents))
                .thenReturn(studentsGroup1AndGroup2);
        when(generatePairsHelper.getPreviousPairs())
                .thenReturn(previousPairsGroup1AndGroup2);
        when(generatePairsHelper.getGroupsWithPossibleOpponents(studentsGroup1AndGroup2))
                .thenReturn(groupsWithOpponents);

        //when
        GeneratePairsResponseDto responseDto = studentsPairsService.generatePairs(generatePairsRequestDto);
        List<Long> unpairedStudentsIds = responseDto.getUnpairedStudentsIds();

        //then
        verify(generatePairsHelper).getPairOrCreateIfNotExist(expectedPairsGroup1AndGroup2);
        Assertions.assertThat(unpairedStudentsIds)
                .as("Unpaired students list must be empty but it has %d size", unpairedStudentsIds.size())
                .isEmpty();
    }

    @Test
    @Description("Given: 2 absent students. Then: 2 pairs and 0 unpaired students")
    void absentAndNoUnpaired() {
        //given
        when(generatePairsRequestDto.getGroupsIds()).thenReturn(group1AndGroup2Ids);
        when(generatePairsRequestDto.getAbsentStudents()).thenReturn(absentStudentsIdsGroup1AndGroup2);

        List<Long> absentStudents = generatePairsRequestDto.getAbsentStudents();

        when(generatePairsHelper.getStudentsFromGroups(group1AndGroup2Ids))
                .thenReturn(studentsGroup1AndGroup2);
        when(generatePairsHelper.getPresentStudents(studentsGroup1AndGroup2, absentStudents))
                .thenReturn(studentsGroup1AndGroup2);
        when(generatePairsHelper.getPreviousPairs())
                .thenReturn(previousPairsGroup1AndGroup2);
        when(generatePairsHelper.getGroupsWithPossibleOpponents(studentsGroup1AndGroup2))
                .thenReturn(groupsWithOpponents);

        //when
        GeneratePairsResponseDto responseDto = studentsPairsService.generatePairs(generatePairsRequestDto);
        List<Long> unpairedStudentsIds = responseDto.getUnpairedStudentsIds();

        //then
        verify(generatePairsHelper).getPairOrCreateIfNotExist(expectedPairsGroup1AndGroup2);
        Assertions.assertThat(unpairedStudentsIds)
                .as("Unpaired students list must be empty but it has %d size", unpairedStudentsIds.size())
                .isEmpty();
    }

    @Test
    @Description("Given: 2 absent students, 2 students without pair in group 1" +
            " and based on previous pairs Then: 2 pairs and 1 unpaired students")
    void absentAndTwoUnpaired() {
        //given
        when(generatePairsRequestDto.getGroupsIds()).thenReturn(group1AndGroup2Ids);
        when(generatePairsRequestDto.getAbsentStudents()).thenReturn(absentStudentsIdsGroup1AndGroup2);

        List<Long> absentStudents = generatePairsRequestDto.getAbsentStudents();

        when(generatePairsHelper.getStudentsFromGroups(group1AndGroup2Ids))
                .thenReturn(allStudentsGroup1AndGroup2PlusUnpaired);
        when(generatePairsHelper.getPresentStudents(allStudentsGroup1AndGroup2PlusUnpaired, absentStudents))
                .thenReturn(studentsGroup1AndGroup2PlusUnpaired);
        when(generatePairsHelper.getPreviousPairs())
                .thenReturn(previousPairsGroup1AndGroup2);
        when(generatePairsHelper.getGroupsWithPossibleOpponents(studentsGroup1AndGroup2PlusUnpaired))
                .thenReturn(groupsWithOpponentsGroup1And2);

        //when
        GeneratePairsResponseDto responseDto = studentsPairsService.generatePairs(generatePairsRequestDto);
        List<Long> unpairedStudentsIdsResult = responseDto.getUnpairedStudentsIds();

        //then
        verify(generatePairsHelper).getPairOrCreateIfNotExist(expectedPairsGroup1AndGroup2);
        Assertions.assertThat(unpairedStudentsIdsResult)
                .as("Must be list with two unpaired students but list has %d size",
                        unpairedStudentsIdsResult.size())
                .hasSize(2);

        Assertions.assertThat(unpairedStudentsIdsResult)
                .as("ID Mismatch. Required ids %s but get %s",
                        unpairedStudentsIds.toString(), unpairedStudentsIdsResult.toString())
                .containsAll(unpairedStudentsIds);
    }

    @Test
    void generatePairsForThreeGroupsWithAbsentAndUnpairs() {
        //given
        when(generatePairsRequestDto.getGroupsIds()).thenReturn(allGroupsIds);
        when(generatePairsRequestDto.getAbsentStudents()).thenReturn(allAbsentStudentsIds);

        List<Long> absentStudents = generatePairsRequestDto.getAbsentStudents();

        when(generatePairsHelper.getStudentsFromGroups(allGroupsIds))
                .thenReturn(allStudents);
        when(generatePairsHelper.getPresentStudents(allStudents, absentStudents))
                .thenReturn(allPresentStudents);
        when(generatePairsHelper.getPreviousPairs())
                .thenReturn(previousPairs);
        when(generatePairsHelper.getGroupsWithPossibleOpponents(allPresentStudents))
                .thenReturn(groupsWithOpponents);

        //when
        GeneratePairsResponseDto responseDto = studentsPairsService.generatePairs(generatePairsRequestDto);
        List<Long> unpairedStudentsIdsResult = responseDto.getUnpairedStudentsIds();

        //then
    }
}