package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.enums.StudentTable;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.StudentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepositoryImpl implements StudentRepository {
    private final DBConnection dbConnection;
    private final static String TABLE_NAME = StudentTable.TABLE_NAME.getName();
    private final static String ID_COL = StudentTable.ID.getName();;
    private final static String FIRST_NAME_COL = StudentTable.FIRST_NAME.getName();;
    private final static String LAST_NAME_COL = StudentTable.LAST_NAME.getName();;
    private final static String GROUP_ID_COL = StudentTable.GROUP_ID.getName();;
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

    private StudentRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class StudentRepositorySingleton {
        private static final StudentRepository INSTANCE = new StudentRepositoryImpl();
    }

    public static StudentRepository getInstance() {
        return StudentRepositorySingleton.INSTANCE;
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
                students.add(extractStudent(resultSet));
            }

            return students;
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)
        ) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                return extractStudentIfPresent(resultSet);
            }
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
                return extractStudentIfPresent(resultSet);
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

    private Optional<Student> extractStudentIfPresent(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(extractStudent(resultSet));
        }

        return Optional.empty();
    }

    private Student extractStudent(ResultSet resultSet) throws SQLException {
        return Student.builder()
                .id(resultSet.getLong(StudentTable.ID.getName()))
                .firstName(resultSet.getString(StudentTable.FIRST_NAME.getName()))
                .lastName(resultSet.getString(StudentTable.LAST_NAME.getName()))
                .groupId(resultSet.getLong(StudentTable.GROUP_ID.getName()))
                .build();
    }
}
