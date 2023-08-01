package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.StudentRepository;
import org.example.quizMates.service.StudentService;

import java.util.List;

@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final static String USER_NOT_FOUND = "User with id %s not found";

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createStudent(CreateStudentDto dto) {
        studentRepository.createStudent(dto);
    }

    @Override
    public void updateStudent(UpdateStudentDto dto) {
        findById(dto.getId());
        studentRepository.updateStudent(dto);
    }
}
