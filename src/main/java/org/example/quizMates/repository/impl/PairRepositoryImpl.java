package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.enums.PairTable;
import org.example.quizMates.exception.DBInternalException;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.PairRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PairRepositoryImpl implements PairRepository {
    private final DBConnection dbConnection;
    private final static String TABLE_NAME = PairTable.TABLE_NAME.getName();
    private final static String ID_COL = PairTable.ID.getName();
    private final static String STUDENT_A_COL = PairTable.STUDENT_A.getName();
    private final static String STUDENT_B_COL = PairTable.STUDENT_B.getName();
    private final static String SELECT_BY_ID_SQL = String.format("SELECT * FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private final static String CREATE_SQL = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)",
            TABLE_NAME, STUDENT_A_COL, STUDENT_B_COL);
    private final static String UPDATE_SQL = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, STUDENT_A_COL, STUDENT_B_COL, ID_COL);
    private final static String DELETE_SQL = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String BY_STUDENT_A_AND_STUDENT_B_SQL = String.format(
            "SELECT * FROM %s WHERE %s IN(?, ?) AND %s IN(?, ?)",
            TABLE_NAME, STUDENT_A_COL, STUDENT_B_COL);

    private PairRepositoryImpl() {
        this.dbConnection = DBConnectionDriverManager.getInstance();
    }

    private static class PairRepositorySingleton {
        private static final PairRepository INSTANCE = new PairRepositoryImpl();
    }

    public static PairRepository getInstance() {
        return PairRepositorySingleton.INSTANCE;
    }

    @Override
    public List<Pair> findAll() {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            List<Pair> allPairs = new ArrayList<>();

            while (resultSet.next()) {
                allPairs.add(extractPair(resultSet));
            }

            return allPairs;
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public Optional<Pair> findById(Long id) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL)
        ) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return extractPairIfPresent(resultSet);
            }
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public Optional<Pair> findByStudentAAndStudentB(Long studentA, Long studentB) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(BY_STUDENT_A_AND_STUDENT_B_SQL)
        ) {
            preparedStatement.setLong(1, studentA);
            preparedStatement.setLong(2, studentB);
            preparedStatement.setLong(3, studentB);
            preparedStatement.setLong(4, studentA);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return extractPairIfPresent(resultSet);
            }
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public List<Pair> findPairsByIds(List<Long> ids) {
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(generateSQLToFetchPairsByIds(ids))
        ) {
            List<Pair> allPairs = new ArrayList<>();
            setLongPlaceholders(statement, ids);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allPairs.add(extractPair(resultSet));
                }

                return allPairs;
            }
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public List<Pair> findPairsByStudents(List<CreatePairDto> dtos) {
        List<Long> studentsAIds = dtos.stream().map(CreatePairDto::getStudentA).toList();
        List<Long> studentsBIds = dtos.stream().map(CreatePairDto::getStudentB).toList();

        String SELECT_PAIRS_BY_STUDENTS_SQL = generateSQLToFetchPairsByStudents(studentsAIds, studentsBIds);
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_PAIRS_BY_STUDENTS_SQL)
        ) {
            List<Pair> allPairs = new ArrayList<>();

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    allPairs.add(extractPair(resultSet));
                }

                return allPairs;
            }
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public void createPair(CreatePairDto dto) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL)
        ) {
            preparedStatement.setLong(1, dto.getStudentA());
            preparedStatement.setLong(2, dto.getStudentB());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public void updatePair(UpdatePairDto dto) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)
        ) {
            preparedStatement.setLong(1, dto.getId());
            preparedStatement.setLong(2, dto.getStudentA());
            preparedStatement.setLong(3, dto.getStudentB());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (
                Connection connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    private Optional<Pair> extractPairIfPresent(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(extractPair(resultSet));
        }

        return Optional.empty();
    }

    private Pair extractPair(ResultSet resultSet) throws SQLException {
        return Pair.builder()
                .id(resultSet.getLong(ID_COL))
                .studentA(resultSet.getLong(STUDENT_A_COL))
                .studentB(resultSet.getLong(STUDENT_B_COL))
                .build();
    }

    private String generateSQLToFetchPairsByIds(List<Long> ids) {
        return "SELECT * FROM " +
                TABLE_NAME +
                " WHERE " +
                ID_COL +
                " IN(" +
                generatePlaceholders(ids.size()) +
                ")";
    }

    private String generateSQLToFetchPairsByStudents(List<Long> studentsAIds, List<Long> studentsBIds) {
        if (studentsAIds.size() != studentsBIds.size())
            throw new RuntimeException("Different list sizes");

        int index = 0;
        Long firstStudentAId = studentsAIds.get(index);
        Long firstStudentBId = studentsBIds.get(index);

        String firstPart = String.format("((%s = %s AND %s = %s) OR (%s = %s AND %s = %s))",
                STUDENT_A_COL, firstStudentAId, STUDENT_B_COL, firstStudentBId,
                STUDENT_B_COL, firstStudentAId, STUDENT_A_COL, firstStudentBId);

        List<String> orParts = new ArrayList<>();
        if (studentsAIds.size() > 1) {
            for(int i = index + 1; i < studentsAIds.size(); i++) {
                firstStudentAId = studentsAIds.get(i);
                firstStudentBId = studentsBIds.get(i);

                String orPart = String.format("OR ((%s = %s AND %s = %s) OR (%s = %s AND %s = %s))",
                        STUDENT_A_COL, firstStudentAId, STUDENT_B_COL, firstStudentBId,
                        STUDENT_B_COL, firstStudentAId, STUDENT_A_COL, firstStudentBId);
                orParts.add(orPart);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(TABLE_NAME);
        sb.append(" WHERE ");
        sb.append(firstPart);


        if (!orParts.isEmpty()) {
            orParts.forEach(sb::append);
        }

        return sb.toString();
    }

    private String generatePlaceholders(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("?");
            if (i < count - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private void setLongPlaceholders(PreparedStatement statement, List<Long> values) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            statement.setLong(i + 1, values.get(i));
        }
    }
}
