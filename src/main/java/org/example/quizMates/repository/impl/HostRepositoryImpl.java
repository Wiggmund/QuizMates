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
    private static final String ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String TABLE_NAME = "hosts";
    private static final String SELECT_ALL_SQL =String.format("SELECT * FROM %s", TABLE_NAME);
    private final DBConnection dbConnection;

    @Override
    public Optional<Host> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Host> findAll()   {
        List<Host> hosts = new ArrayList<>();

        try(Connection connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
            ResultSet resultSet = ps.executeQuery()) {

            while(resultSet.next()){
                Host host = Host.builder()
                        .id(resultSet.getLong(ID))
                        .firstName(resultSet.getString(FIRST_NAME))
                        .lastName(resultSet.getString(LAST_NAME))
                        .build();
                hosts.add(host);
            }

        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
        return hosts;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createHost(CreateHostDto dto) {

    }

    @Override
    public void updateHost(UpdateHostDto dto) {

    }
}
