package org.example.quizMates.repository;

import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.model.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
    void createStudent(CreateStudentDto dto);
    void updateStudent(UpdateStudentDto dto);
}
