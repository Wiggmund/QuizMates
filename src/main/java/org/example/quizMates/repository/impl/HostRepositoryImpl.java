package org.example.quizMates.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnection;
import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Host;
import org.example.quizMates.repository.HostRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HostRepositoryImpl implements HostRepository {
    private static final String ID_COL = "id";
    private static final String FIRST_NAME_COL = "first_name";
    private static final String LAST_NAME_COL = "last_name";
    private static final String TABLE_NAME = "hosts";
    private static final String SELECT_ALL_SQL =String.format("SELECT * FROM %s", TABLE_NAME);
    private static final String CREATE_USER_SQL =String.format("INSERT INTO hosts (%s, %s) VALUES (?, ?)",
            FIRST_NAME_COL, LAST_NAME_COL);
    private static final String UPDATE_HOST_SQL = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, FIRST_NAME_COL, LAST_NAME_COL, ID_COL);
    private static final String DELETE_HOST_SQL = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);
    private static final String SELECT_HOST_BY_ID_SQL = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);

    private final DBConnection dbConnection;

    @Override
    public Optional<Host> findById(Long id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_HOST_BY_ID_SQL)) {

            ps.setLong(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Host host = Host.builder()
                            .id(resultSet.getLong(ID_COL))
                            .firstName(resultSet.getString(FIRST_NAME_COL))
                            .lastName(resultSet.getString(LAST_NAME_COL))
                            .build();
                    return Optional.of(host);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public List<Host> findAll()   {
        List<Host> hosts = new ArrayList<>();

        try(Connection connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
            ResultSet resultSet = ps.executeQuery()) {

            while(resultSet.next()){
                Host host = Host.builder()
                        .id(resultSet.getLong(ID_COL))
                        .firstName(resultSet.getString(FIRST_NAME_COL))
                        .lastName(resultSet.getString(LAST_NAME_COL))
                        .build();
                hosts.add(host);
            }

        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
        return hosts;
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_HOST_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void createHost(CreateHostDto dto) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_USER_SQL)) {

            ps.setString(1, dto.getFirstName());
            ps.setString(2, dto.getLastName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }


    @Override
    public void updateHost(UpdateHostDto dto) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_HOST_SQL)) {

            ps.setString(1, dto.getFirstName());
            ps.setString(2, dto.getLastName());
            ps.setLong(3, dto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }
}
