package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRepositoryImpl implements SessionRepository {
    private static final String TABLE_NAME = "sessions";
    private final static String ID_COL = "id";
    private static final String TITLE_COL = "title";
    private final static String DESCRIPTION_COL = "description";
    private final static String DATE_COL = "date";
    private final static String BEST_STUDENT_COL = "best_student";
    private final static String BEST_GROUP_COL = "best_group";
    private final static String STATUS_COL = "status";
    private static final String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private static final String SELECT_SESSION_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME,
            ID_COL);
    private final static String CREATE_SESSIONS_SQL = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES(?,?,?,?,?,?)",
            TABLE_NAME, TITLE_COL, DESCRIPTION_COL, DATE_COL, BEST_STUDENT_COL, BEST_GROUP_COL, STATUS_COL);
    private final static String UPDATE_SQL = String.format(
            "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?,  %s = ?  WHERE %s = ?",
            TABLE_NAME, TITLE_COL, DESCRIPTION_COL, DATE_COL, BEST_STUDENT_COL, BEST_GROUP_COL, STATUS_COL, ID_COL);
    private static final String DELETE_SQL = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);

    private final DBConnection dbConnection;

    private SessionRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class SessionRepositorySingleton {
        private static final SessionRepository INSTANCE = new SessionRepositoryImpl();
    }

    public static SessionRepository getInstance() {
        return SessionRepositorySingleton.INSTANCE;
    }

    @Override
    public Optional<Session> findById(Long id) {
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SELECT_SESSION_BY_ID)
        ) {
            ps.setLong(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Session fetchedSession = Session.builder()
                            .id(resultSet.getLong(ID_COL))
                            .title(resultSet.getString(TITLE_COL))
                            .description(resultSet.getString(DESCRIPTION_COL))
                            .date(resultSet.getTimestamp(DATE_COL).toLocalDateTime())
                            .best_student(resultSet.getLong(BEST_STUDENT_COL))
                            .best_group(resultSet.getLong(BEST_GROUP_COL))
                            .status(resultSet.getBoolean(STATUS_COL))
                            .build();

                    return Optional.of(fetchedSession);
                }

                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public List<Session> findAll() {
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<Session> sessions = new ArrayList<>();

            while (resultSet.next()) {
                Session fetchedSession = Session.builder()
                        .id(resultSet.getLong(ID_COL))
                        .title(resultSet.getString(TITLE_COL))
                        .description(resultSet.getString(DESCRIPTION_COL))
                        .date(resultSet.getTimestamp(DATE_COL).toLocalDateTime())
                        .best_student(resultSet.getLong(BEST_STUDENT_COL))
                        .best_group(resultSet.getLong(BEST_GROUP_COL))
                        .status(resultSet.getBoolean(STATUS_COL))
                        .build();

                sessions.add(fetchedSession);
            }
            return sessions;
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
    public Optional<Session> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public void createSession(CreateSessionDto dto) {
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATE_SESSIONS_SQL)
        ) {
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(dto.getDate()));
            ps.setLong(4, dto.getBestStudent());
            ps.setLong(5, dto.getBestGroup());
            ps.setBoolean(6, dto.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }

    }

    @Override
    public void updateSession(UpdateSessionDto dto) {
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1, dto.getTitle());
            statement.setString(2, dto.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(dto.getDate()));
            statement.setLong(4, dto.getBestStudent());
            statement.setLong(5, dto.getBestGroup());
            statement.setBoolean(6, dto.getStatus());
            statement.setLong(7, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }
}
