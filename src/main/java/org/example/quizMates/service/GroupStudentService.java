package org.example.quizMates.service;

import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.model.Student;

import java.util.List;

public interface GroupStudentService {
    List<Student> getAllGroupStudents();
    void addStudentToGroup(AddStudentToGroupDto dto);
    void removeStudentFromGroup(RemoveStudentFromGroupDto dto);
}
