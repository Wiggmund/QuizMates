package org.example.quizMates.service.impl;

import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.dto.group.UpdateGroupStudentAmount;
import org.example.quizMates.exception.ResourceAlreadyExistException;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Group;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.GroupStudentRepository;
import org.example.quizMates.repository.impl.GroupStudentRepositoryImpl;
import org.example.quizMates.service.GroupService;
import org.example.quizMates.service.GroupStudentService;
import org.example.quizMates.service.StudentService;

import java.util.List;
import java.util.Objects;

public class GroupStudentServiceImpl implements GroupStudentService {
    private final GroupStudentRepository groupStudentRepository;
    private final StudentService studentService;
    private final GroupService groupService;
    private final static String STUDENT_ALREADY_HAS_GROUP = "Student %s is already in group %s";
    private final static String STUDENT_WRONG_GROUP = "Student %s is not in group %s";

    private GroupStudentServiceImpl() {
        this.groupStudentRepository = GroupStudentRepositoryImpl.getInstance();
        this.groupService = GroupServiceImpl.gteInstance();
        this.studentService = StudentServiceImpl.getInstance();
    }

    private static class GroupStudentServiceImplSingleton {
        private static final GroupStudentService INSTANCE = new GroupStudentServiceImpl();
    }

    public static GroupStudentService getInstance() {
        return GroupStudentServiceImplSingleton.INSTANCE;
    }

    @Override
    public List<Student> getAllGroupStudents(Long id) {
        return groupStudentRepository.getAllGroupStudents(id);
    }

    @Override
    public void addStudentToGroup(AddStudentToGroupDto dto) {
        Student student = studentService.findById(dto.getStudentId());
        Group group = groupService.findById(dto.getGroupId());

        if(student.getGroupId() != 0) {
            throw new ResourceAlreadyExistException(String.format(STUDENT_ALREADY_HAS_GROUP,
                    student.getFullName(), group.getName()));
        }
        groupStudentRepository.addStudentToGroup(dto);
        groupService.updateGroupStudentAmount(UpdateGroupStudentAmount.builder()
                        .studentsAmount(group.getStudentsAmount() + 1)
                        .build());
    }

    @Override
    public void removeStudentFromGroup(RemoveStudentFromGroupDto dto) {
        Student student = studentService.findById(dto.getStudentId());
        Group group = groupService.findById(dto.getGroupId());

        if(!Objects.equals(group.getId(), student.getGroupId())){
            throw new ResourceNotFoundException(String.format(STUDENT_WRONG_GROUP,
                    student.getFullName(), group.getName()));
        }

        groupStudentRepository.removeStudentFromGroup(dto);
        groupService.updateGroupStudentAmount(UpdateGroupStudentAmount.builder()
                .studentsAmount(group.getStudentsAmount() - 1)
                .build());
    }
}
