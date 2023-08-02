package org.example.quizMates.repository.impl;

import org.example.quizMates.db.DBConnection;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
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
    private final static String TABLE_NAME = "pairs";
    private final static String ID_COL = "id";
    private final static String STUDENT_A_COL = "student_a";
    private final static String STUDENT_B_COL = "student_b";
    private final static String SELECT_BY_ID_SQL = String.format("SELECT * FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);
    private final static String SELECT_ALL_SQL = String.format("SELECT * FROM %s", TABLE_NAME);
    private final static String CREATE_SQL = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)",
            TABLE_NAME, STUDENT_A_COL, STUDENT_B_COL);
    private final static String UPDATE_SQL = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, STUDENT_A_COL, STUDENT_B_COL, ID_COL);
    private final static String DELETE_SQL = String.format("DELETE FROM %s WHERE %s = ?",
            TABLE_NAME, ID_COL);

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
    public Optional<Pair> findByStudentAAndStudentB(Long studentA, Long studentB) {
        return Optional.empty();
    }

    @Override
    public void createPair(CreatePairDto dto) {
        try (Connection connection = dbConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL);
        ) {
            preparedStatement.setLong(1, dto.getStudentA());
            preparedStatement.setLong(2, dto.getStudentB());
            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public void updatePair(UpdatePairDto dto) {
        try (
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
        ) {
            preparedStatement.setLong(1, dto.getId());
            preparedStatement.setLong(2, dto.getStudentA());
            preparedStatement.setLong(3, dto.getStudentB());
            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            throw new DBInternalException(ex.getMessage());
        }
    }

    @Override
    public List<Pair> findAll() {
        try(
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            List<Pair> allPairs = new ArrayList<>();

            while (resultSet.next()) {
                Pair pair = Pair.builder()
                        .id(resultSet.getLong(ID_COL))
                        .studentA(resultSet.getString(STUDENT_A_COL))
                        .studentB(resultSet.getString(STUDENT_B_COL))
                        .build();

                allPairs.add(pair);
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
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL);
        ) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Pair pair = Pair.builder()
                            .id(resultSet.getLong(ID_COL))
                            .studentA(resultSet.getString(STUDENT_A_COL))
                            .studentB(resultSet.getString(STUDENT_B_COL))
                            .build();

                    return Optional.of(pair);
                }
                return Optional.empty();
            }
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
}
