package org.example.quizMates.service;

import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.model.Student;

public interface StudentService extends CrudService<Student, Long> {
    void createStudent(CreateStudentDto dto);
    void updateStudent(UpdateStudentDto dto);
}