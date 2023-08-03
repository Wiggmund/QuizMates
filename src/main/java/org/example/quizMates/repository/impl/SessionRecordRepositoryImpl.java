package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.enums.SessionRecordTable;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.repository.SessionRecordRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRecordRepositoryImpl implements SessionRecordRepository{
    private static final String TABLE_NAME = SessionRecordTable.TABLE_NAME.getName();
    private final static String ID_COL = SessionRecordTable.ID.getName();
    private final static String SESSION_ID_COL = SessionRecordTable.SESSION_ID.getName();
    private static final String PAIR_ID_COL = SessionRecordTable.PAIR_ID.getName();
    private final static String STUDENT_ID_COL = SessionRecordTable.STUDENT_ID.getName();
    private final static String HOST_ID_COL = SessionRecordTable.HOST_ID.getName();
    private final static String SCORE_COL = SessionRecordTable.SCORE.getName();
    private final static String HOST_NOTES_COL = SessionRecordTable.HOST_NOTES.getName();
    private final static String WAS_PRESENT_COL = SessionRecordTable.WAS_PRESENT.getName();
    private static final String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private static final String SELECT_SESSION_RECORD_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);
    private static final String SELECT_SESSION_RECORD_BY_STUDENT_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, STUDENT_ID_COL);
    private static final String SELECT_SESSION_RECORD_BY_SESSION_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME,SESSION_ID_COL);
    private final static String CREATE_SESSION_RECORD_SQL = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES(?,?,?,?,?,?,?)",
            TABLE_NAME, SESSION_ID_COL, PAIR_ID_COL, STUDENT_ID_COL, HOST_ID_COL, SCORE_COL, HOST_NOTES_COL, WAS_PRESENT_COL);
    private final static String UPDATE_SQL = String.format(
            "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?  WHERE %s = ?",
            TABLE_NAME, SESSION_ID_COL, PAIR_ID_COL, STUDENT_ID_COL, HOST_ID_COL, SCORE_COL, HOST_NOTES_COL, WAS_PRESENT_COL, ID_COL);
    private static final String DELETE_SQL = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);

    private final DBConnection dbConnection;

    private SessionRecordRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class SessionRecordRepositorySingleton {
        private static final SessionRecordRepository INSTANCE = new SessionRecordRepositoryImpl();
    }

    public static SessionRecordRepository getInstance() {
        return SessionRecordRepositoryImpl.SessionRecordRepositorySingleton.INSTANCE;
    }

    @Override
    public List<SessionRecord> findAll() {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()
        ) {
            List<SessionRecord> sessionRecords = new ArrayList<>();

            while (resultSet.next()) {
                SessionRecord fetchedSessionRecord = extractSessionRecord(resultSet);
                sessionRecords.add(fetchedSessionRecord);
            }
            return sessionRecords;
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public Optional<SessionRecord> findById(Long id) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_SESSION_RECORD_BY_ID)
        ) {
            ps.setLong(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                return extractSessionRecordIfPresent(resultSet);
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public List <SessionRecord> findByStudentId(Long id) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_SESSION_RECORD_BY_STUDENT_ID);

        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()){
                List<SessionRecord> sessionRecords = new ArrayList<>();
                while (resultSet.next()) {
                    SessionRecord fetchedSessionRecord = extractSessionRecord(resultSet);

                    sessionRecords.add(fetchedSessionRecord);
                }
                return sessionRecords;
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public List<SessionRecord> findBySessionId(Long id) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_SESSION_RECORD_BY_SESSION_ID);

        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()){
                List<SessionRecord> sessionRecords = new ArrayList<>();
                while (resultSet.next()) {
                    SessionRecord fetchedSessionRecord = extractSessionRecord(resultSet);

                    sessionRecords.add(fetchedSessionRecord);
                }
                return sessionRecords;
            }
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void createSessionRecord(CreateSessionRecordDto dto) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(CREATE_SESSION_RECORD_SQL)
        ) {
            ps.setDouble(1, dto.getSessionId());
            ps.setDouble(2, dto.getPairId());
            ps.setDouble(3, dto.getStudentId());
            ps.setDouble(4, dto.getHostId());
            ps.setDouble(5, dto.getScore());
            ps.setString(6, dto.getHostNotes());
            ps.setBoolean(7, dto.getWasPresent());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBInternalException(e.getMessage());
        }
    }

    @Override
    public void updateSessionRecord(UpdateSessionRecordDto dto) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)
        ) {
            ps.setDouble(1, dto.getSessionId());
            ps.setDouble(2, dto.getPairId());
            ps.setDouble(3, dto.getStudentId());
            ps.setDouble(4, dto.getHostId());
            ps.setDouble(5, dto.getScore());
            ps.setString(6, dto.getHostNotes());
            ps.setBoolean(7, dto.getWasPresent());
            ps.setLong(8,dto.getId());
            ps.executeUpdate();
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
    private Optional<SessionRecord> extractSessionRecordIfPresent(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(extractSessionRecord(resultSet));
        }

        return Optional.empty();
    }

    private SessionRecord extractSessionRecord(ResultSet resultSet) throws SQLException {
        return SessionRecord.builder()
                .sessionId((resultSet.getLong(SessionRecordTable.SESSION_ID.getName())))
                .pairId(resultSet.getLong(SessionRecordTable.PAIR_ID.getName()))
                .studentId(resultSet.getLong(SessionRecordTable.STUDENT_ID.getName()))
                .hostId(resultSet.getLong(SessionRecordTable.HOST_ID.getName()))
                .score(resultSet.getDouble(SessionRecordTable.SCORE.getName()))
                .hostNotes(resultSet.getString(SessionRecordTable.HOST_NOTES.getName()))
                .wasPresent(resultSet.getBoolean(SessionRecordTable.WAS_PRESENT.getName()))
                .build();
    }
}
