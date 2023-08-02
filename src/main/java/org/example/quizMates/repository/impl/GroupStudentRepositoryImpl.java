package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.group.AddStudentToGroupDto;
import org.example.quizMates.dto.group.RemoveStudentFromGroupDto;
import org.example.quizMates.enums.StudentTable;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.GroupStudentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupStudentRepositoryImpl implements GroupStudentRepository {
    private final static String STUDENT_TABLE_NAME = StudentTable.TABLE_NAME.getName();
    private final static String STUDENT_ID_COL = StudentTable.ID.getName();
    private final static String STUDENT_FIRST_NAME_COL = StudentTable.FIRST_NAME.getName();
    private final static String STUDENT_LAST_NAME_COL = StudentTable.LAST_NAME.getName();
    private final static String STUDENT_GROUP_ID_COL = StudentTable.GROUP_ID.getName();
    private final static String SELECT_ALL_GROUP_STUDENTS_SQL = String.format("SELECT * FROM %s WHERE %s = ?",
            STUDENT_TABLE_NAME, STUDENT_GROUP_ID_COL);
    private final static String ADD_STUDENT_TO_GROUP_SQL = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
            STUDENT_TABLE_NAME, STUDENT_GROUP_ID_COL, STUDENT_ID_COL);
    private final static String REMOVE_STUDENT_FROM_GROUP_SQL = String.format("UPDATE %s SET %s = NULL WHERE %s = ?",
            STUDENT_TABLE_NAME, STUDENT_GROUP_ID_COL, STUDENT_ID_COL);

    private final DBConnection dbConnection;

    private GroupStudentRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class GroupStudentRepositoryImplSingleton {
        private static final GroupStudentRepository INSTANCE = new GroupStudentRepositoryImpl();
    }

    public static GroupStudentRepository getInstance() {
        return GroupStudentRepositoryImplSingleton.INSTANCE;
    }

    @Override
    public List<Student> getAllGroupStudents(Long id) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_GROUP_STUDENTS_SQL)
        ) {
            List<Student> students = new ArrayList<>();
            statement.setLong(1, id);
            
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student fetchedStudent = Student.builder()
                            .id(resultSet.getLong(STUDENT_ID_COL))
                            .firstName(resultSet.getString(STUDENT_FIRST_NAME_COL))
                            .lastName(resultSet.getString(STUDENT_LAST_NAME_COL))
                            .groupId(resultSet.getLong(STUDENT_GROUP_ID_COL))
                            .build();

                    students.add(fetchedStudent);
                }
                return students;
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void addStudentToGroup(AddStudentToGroupDto dto) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(ADD_STUDENT_TO_GROUP_SQL)
        ) {
            statement.setLong(1, dto.getGroupId());
            statement.setLong(2, dto.getStudentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void removeStudentFromGroup(RemoveStudentFromGroupDto dto) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(REMOVE_STUDENT_FROM_GROUP_SQL)
        ) {
            statement.setLong(1, dto.getStudentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }
}
