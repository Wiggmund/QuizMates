package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.StudentRepository;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.StudentService;

import java.util.List;

@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final DuplicationService duplicationService;
    private final static String STUDENT_NOT_FOUND = "Student with id %s not found";
    private final static String STUDENT_DUPLICATE_NAME = "Student with firstName %s and lastName %s already exists";


    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND, id)));
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        studentRepository.deleteById(id);
    }

    @Override
    public void createStudent(CreateStudentDto dto) {
        if (duplicationService.doTheSameStudentExist(dto.getFirstName(), dto.getLastName())) {
            throw new ResourceNotFoundException(String.format(STUDENT_DUPLICATE_NAME,
                    dto.getFirstName(), dto.getLastName()));
        }
        studentRepository.createStudent(dto);
    }

    @Override
    public void updateStudent(UpdateStudentDto dto) {
        Student student = findById(dto.getId());

        boolean doFirstNameTheSame = dto.getFirstName().equalsIgnoreCase(student.getFirstName());
        boolean doLastNameTheSame = dto.getLastName().equalsIgnoreCase(student.getLastName());

        if (!doFirstNameTheSame || !doLastNameTheSame) {
            boolean doTheSameStudentExist = duplicationService.doTheSameStudentExist(dto.getFirstName(), dto.getLastName());

            if (doTheSameStudentExist) {
                throw new ResourceNotFoundException(
                        String.format(STUDENT_DUPLICATE_NAME, dto.getFirstName(), dto.getLastName()));
            }
        }
        studentRepository.updateStudent(dto);
    }
}
