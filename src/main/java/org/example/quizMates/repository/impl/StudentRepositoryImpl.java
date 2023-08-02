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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {
    private final DBConnection dbConnection;
    private final static String TABLE_NAME = "students";
    private final static String ID_COL = "id";
    private final static String FIRST_NAME_COL = "first_name";
    private final static String LAST_NAME_COL = "last_name";
    private final static String SELECT_BY_ID_SQL = String.format("SELECT * FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private final static String CREATE_SQL = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)",
            TABLE_NAME, FIRST_NAME_COL, LAST_NAME_COL);
    private final static String UPDATE_SQL = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, FIRST_NAME_COL, LAST_NAME_COL, ID_COL);
    private final static String DELETE_SQL = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String SELECT_BY_FIRST_AND_LAST_NAME_SQL = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
            TABLE_NAME, FIRST_NAME_COL, LAST_NAME_COL);

    @Override
    public Optional<Student> findById(Long id) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)
        ) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Student fetchedStudent = Student.builder()
                            .id(resultSet.getLong(ID_COL))
                            .firstName(resultSet.getString(FIRST_NAME_COL))
                            .lastName(resultSet.getString(LAST_NAME_COL))
                            .build();

                    return Optional.of(fetchedStudent);
                }

                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public List<Student> findAll() {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<Student> students = new ArrayList<>();

            while(resultSet.next()) {
                Student fetchedStudent = Student.builder()
                        .id(resultSet.getLong(ID_COL))
                        .firstName(resultSet.getString(FIRST_NAME_COL))
                        .lastName(resultSet.getString(LAST_NAME_COL))
                        .build();

                students.add(fetchedStudent);
            }

            return students;
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_SQL)
        ) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public Optional<Student> findByFirstNameAndLastName(String firstName, String lastName) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BY_FIRST_AND_LAST_NAME_SQL)
        ) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Student fetchedStudent = Student.builder()
                            .id(resultSet.getLong(ID_COL))
                            .firstName(resultSet.getString(FIRST_NAME_COL))
                            .lastName(resultSet.getString(LAST_NAME_COL))
                            .build();

                    return Optional.of(fetchedStudent);
                }

                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
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
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setLong(3, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }
}
