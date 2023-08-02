package org.example.quizMates.repository;

import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.model.Student;

import java.util.List;

public interface GroupStudentRepository {
    List<Student> getAllGroupStudents();
    void addStudentToGroup(AddStudentToGroupDto dto);
    void removeStudentFromGroup(RemoveStudentFromGroupDto dto);
}
