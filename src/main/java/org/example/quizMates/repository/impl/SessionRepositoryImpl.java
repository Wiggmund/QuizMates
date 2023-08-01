package org.example.quizMates.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnection;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {
    private static final String TABLE_NAME = "sessions";
    private final static String ID_COL = "id";
    private static final String TITLE_NAME = "title";
    private final static String DESCRIPTION_COL = "description";
    private final static String DATE_COL = "date";
    private final static String BEST_STUDENT_COL = "best_student";
    private final static String BEST_GROUP_COL = "best_group";
    private final static String STATUS_COL = "status";
    private static final String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private static final String SELECT_USER_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);
    private final static String CREATE_SESSIONS_SQL = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) " +
            "VALUES(?,?,?,?,?,?)", TABLE_NAME, TITLE_NAME, DESCRIPTION_COL, DATE_COL, BEST_STUDENT_COL, BEST_GROUP_COL, STATUS_COL);
    //    private final static String UPDATE_USER_SQL = String.format(
//            "UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
//            TABLE_NAME, FIRST_NAME_COL, LAST_NAME_COL, AGE_COL, ID_COL);
    private static final String DELETE_USER_SQL = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);

    private final DBConnection dbConnection;

    @Override
    public Optional<Session> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Session> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

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
    }catch (SQLException e){
            throw new DBInternalException(e.getMessage());
        }

    }

    @Override
    public void updateSession(UpdateSessionDto dto) {
    }
}
