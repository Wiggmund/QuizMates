package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
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

public class GroupRepositoryImpl implements GroupRepository {
    private final DBConnection dbConnection;
    private final static String TABLE_NAME = "groups";
    private final static String ID_COL = "id";
    private final static String NAME_COL = "name";
    private final static String SELECT_BY_ID_SQL = String.format("SELECT * FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private final static String CREATE_SQL = String.format("INSERT INTO %s(%s) VALUES(?)",
            TABLE_NAME, NAME_COL);
    private final static String UPDATE_SQL = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
            TABLE_NAME, NAME_COL, ID_COL);
    private final static String DELETE_SQL = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);

    private GroupRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class GroupRepositorySingleton {
        private static final GroupRepository INSTANCE = new GroupRepositoryImpl();
    }

    public static GroupRepository getInstance() {
        return GroupRepositorySingleton.INSTANCE;
    }

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
    public Optional<Group> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public void createGroup(CreateGroupDto dto) {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_SQL)
        ) {
            statement.setString(1, dto.getName());
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
            statement.setLong(3, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }
}
