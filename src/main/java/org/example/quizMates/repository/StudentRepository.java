package org.example.quizMates.repository;

import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {
    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);
    List<Student> findStudentsByIds(List<Long> ids);
    void createStudent(CreateStudentDto dto);
    void updateStudent(UpdateStudentDto dto);
}
