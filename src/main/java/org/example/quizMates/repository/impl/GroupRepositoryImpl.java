package org.example.quizMates.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnection;
import org.example.quizMates.dto.group.CreateGroupDto;
import org.example.quizMates.dto.group.UpdateGroupDto;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Group;
import org.example.quizMates.repository.GroupRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {
    private final DBConnection dbConnection;
    private final static String TABLE_NAME = "Group";
    private final static String ID_COL = "id";
    private final static String STUDENT_ID_COL = "student_id";
    private final static String NAME_COL = "name";
    private final static String SELECT_BY_ID_SQL = String.format("SELECT * FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private final static String CREATE_SQL = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)",
            TABLE_NAME, STUDENT_ID_COL, NAME_COL);
    private final static String UPDATE_SQL = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, STUDENT_ID_COL, NAME_COL, ID_COL);
    private final static String DELETE_SQL = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);

    @Override
    public Optional<Group> findById(Long id) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)
        ) {
            statement.setLong(1, id);

            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group fetchedGroup = Group.builder()
                            .id(resultSet.getLong(ID_COL))
                            .studentId(resultSet.getString(STUDENT_ID_COL))
                            .name(resultSet.getString(NAME_COL))
                            .build();

                    return Optional.of(fetchedGroup);
                }

                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public List<Group> findAll() {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<Group> groups = new ArrayList<>();

            while(resultSet.next()) {
                Group fetchedGroup = Group.builder()
                        .id(resultSet.getLong(ID_COL))
                        .studentId(resultSet.getString(STUDENT_ID_COL))
                        .name(resultSet.getString(NAME_COL))
                        .build();

                groups.add(fetchedGroup);
            }

            return groups;
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
    public void createGroup(CreateGroupDto dto) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_SQL)
        ) {
            statement.setString(1, dto.getName());
            statement.setString(2, dto.getStudentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void updateGroup(UpdateGroupDto dto) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1, dto.getName());
            statement.setString(2, dto.getStudentId());
            statement.setLong(3, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }
}
