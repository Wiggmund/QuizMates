package org.example.quizMates.service.impl;

import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.model.Student;
import org.example.quizMates.service.StudentService;

import java.util.List;
import java.util.Optional;

public class StudentServiceImpl implements StudentService {
    @Override
    public Optional<Student> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createStudent(CreateStudentDto dto) {

    }

    @Override
    public void updateStudent(UpdateStudentDto dto) {

    }
}
