package org.example.quizMates.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnection;
import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.StudentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {
    private final DBConnection dbConnection;
    private final static String TABLE_NAME = "students";
    private final static String ID_COL = "id";
    private final static String FIRST_NAME_COL = "first_name";
    private final static String LAST_NAME_COL = "last_name";
    private final static String CREATE_SQL = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)",
            TABLE_NAME, FIRST_NAME_COL, LAST_NAME_COL);

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
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_SQL)
        ) {
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void updateStudent(UpdateStudentDto dto) {

    }
}
