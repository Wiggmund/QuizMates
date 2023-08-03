package org.example.quizMates.service;

import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.model.Student;

import java.util.List;

public interface StudentService extends CrudService<Student, Long> {
    List<Student> findStudentsByIds(List<Long> ids);
    void createStudent(CreateStudentDto dto);
    void updateStudent(UpdateStudentDto dto);
}
