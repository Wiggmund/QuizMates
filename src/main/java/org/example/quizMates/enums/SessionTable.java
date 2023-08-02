package org.example.quizMates.enums;

import lombok.Getter;

@Getter
public enum SessionTable {
    TABLE_NAME("sessions"),
    ID("id"),
    TITLE("title"),
    DESCRIPTION("description"),
    DATE("date"),
    BEST_STUDENT("best_student"),
    BEST_GROUP("best_group"),
    STATUS("status");

    private final String name;

    SessionTable(String name) {
        this.name = name;
    }
}
