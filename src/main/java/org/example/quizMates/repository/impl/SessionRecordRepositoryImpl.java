package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.enums.SessionRecordAction;
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
    private final static String ACTION_COL = SessionRecordTable.ACTION.getName();
    private final static String QUESTION_COL = SessionRecordTable.QUESTION.getName();
    private static final String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private static final String SELECT_SESSION_RECORD_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);
    private static final String SELECT_SESSION_RECORD_BY_STUDENT_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, STUDENT_ID_COL);
    private static final String SELECT_SESSION_RECORD_BY_SESSION_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME,SESSION_ID_COL);
    private static final String SELECT_RECORDS_BY_STUDENT_SESSION_ID = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
            TABLE_NAME, STUDENT_ID_COL, SESSION_ID_COL);
    private final static String CREATE_SESSION_RECORD_SQL = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES(?,?,?,?,?,?,?,?,?)",
            TABLE_NAME, SESSION_ID_COL, PAIR_ID_COL, STUDENT_ID_COL, HOST_ID_COL,
            SCORE_COL, HOST_NOTES_COL, WAS_PRESENT_COL, ACTION_COL, QUESTION_COL);
    private final static String UPDATE_SQL = String.format(
            "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?  WHERE %s = ?",
            TABLE_NAME, SESSION_ID_COL, PAIR_ID_COL, STUDENT_ID_COL, HOST_ID_COL,
            SCORE_COL, HOST_NOTES_COL, WAS_PRESENT_COL, ACTION_COL, QUESTION_COL, ID_COL);
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
    public List<SessionRecord> findByStudentIdAndSessionId(Long studentId, Long sessionId) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_RECORDS_BY_STUDENT_SESSION_ID)
        ) {
            ps.setLong(1, studentId);
            ps.setLong(2, sessionId);

            try (ResultSet resultSet = ps.executeQuery()) {
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

            if (dto.getPairId() == null) {
                ps.setNull(2, Types.BIGINT);
            } else {
                ps.setDouble(2, dto.getPairId());
            }

            ps.setDouble(3, dto.getStudentId());
            ps.setDouble(4, dto.getHostId());
            ps.setDouble(5, dto.getScore());

            if(dto.getHostNotes() == null) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, dto.getHostNotes());
            }

            ps.setBoolean(7, dto.getWasPresent());
            ps.setString(8, dto.getAction().name());

            if(dto.getQuestion() == null) {
                ps.setNull(9, Types.VARCHAR);
            } else {
                ps.setString(9, dto.getQuestion());
            }

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
            ps.setString(8, dto.getAction().name());
            ps.setString(9, dto.getQuestion());
            ps.setLong(10,dto.getId());
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
                .id((resultSet.getLong(ID_COL)))
                .sessionId((resultSet.getLong(SESSION_ID_COL)))
                .pairId(resultSet.getLong(PAIR_ID_COL))
                .studentId(resultSet.getLong(STUDENT_ID_COL))
                .hostId(resultSet.getLong(HOST_ID_COL))
                .score(resultSet.getDouble(SCORE_COL))
                .hostNotes(resultSet.getString(HOST_NOTES_COL))
                .wasPresent(resultSet.getBoolean(WAS_PRESENT_COL))
                .action(SessionRecordAction.valueOf(resultSet.getString(ACTION_COL)))
                .question(resultSet.getString(QUESTION_COL))
                .build();
    }
}
