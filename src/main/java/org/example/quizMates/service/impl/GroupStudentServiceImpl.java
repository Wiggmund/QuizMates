package org.example.quizMates.service.impl;

import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.GroupStudentRepository;
import org.example.quizMates.repository.impl.GroupStudentRepositoryImpl;
import org.example.quizMates.service.GroupService;
import org.example.quizMates.service.GroupStudentService;
import org.example.quizMates.service.StudentService;

import java.util.List;

public class GroupStudentServiceImpl implements GroupStudentService {
    private final GroupStudentRepository groupStudentRepository;
    private final StudentService studentService;
    private final GroupService groupService;

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
    public List<Student> getAllGroupStudents() {
        return null;
    }

    @Override
    public void addStudentToGroup(AddStudentToGroupDto dto) {

    }

    @Override
    public void removeStudentFromGroup(RemoveStudentFromGroupDto dto) {

    }
}
