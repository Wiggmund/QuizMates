package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.enums.SessionTable;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRepositoryImpl implements SessionRepository {
    private static final String TABLE_NAME = SessionTable.TABLE_NAME.getName();
    private final static String ID_COL = SessionTable.ID.getName();
    private static final String TITLE_COL = SessionTable.TITLE.getName();
    private final static String DESCRIPTION_COL = SessionTable.DESCRIPTION.getName();
    private final static String DATE_COL = SessionTable.DATE.getName();
    private final static String BEST_STUDENT_COL = SessionTable.BEST_STUDENT.getName();
    private final static String BEST_GROUP_COL = SessionTable.BEST_GROUP.getName();
    private final static String STATUS_COL = SessionTable.STATUS.getName();
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
    private static final String SELECT_SESSION_BY_TITLE = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, TITLE_COL);

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
                return extractSessionIfPresent(resultSet);
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
                Session fetchedSession = extractSession(resultSet);

                sessions.add(fetchedSession);
            }
            return sessions;
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public Optional<Session> findByTitle(String title) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_SESSION_BY_TITLE)
        ) {
            ps.setString(1, title);

            try (ResultSet resultSet = ps.executeQuery()) {
                return extractSessionIfPresent(resultSet);
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
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

    private Optional<Session> extractSessionIfPresent(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(extractSession(resultSet));
        }

        return Optional.empty();
    }

    private Session extractSession(ResultSet resultSet) throws SQLException {
        return Session.builder()
                .id(resultSet.getLong(SessionTable.ID.getName()))
                .title(resultSet.getString(SessionTable.TITLE.getName()))
                .description(resultSet.getString(SessionTable.DESCRIPTION.getName()))
                .date(resultSet.getTimestamp(SessionTable.DATE.getName()).toLocalDateTime())
                .best_student(resultSet.getLong(SessionTable.BEST_STUDENT.getName()))
                .best_group(resultSet.getLong(SessionTable.BEST_GROUP.getName()))
                .status(resultSet.getBoolean(SessionTable.STATUS.getName()))
                .build();
    }
}
