package org.example.quizMates.repository;

import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.model.Student;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);
    void createStudent(CreateStudentDto dto);
    void updateStudent(UpdateStudentDto dto);
}
