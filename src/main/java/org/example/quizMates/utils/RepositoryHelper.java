package org.example.quizMates.utils;

import org.example.quizMates.enums.SessionTable;
import org.example.quizMates.model.Session;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryHelper {
    public static Session extractEntity(ResultSet resultSet, Class<Session> clazz) throws SQLException {
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
