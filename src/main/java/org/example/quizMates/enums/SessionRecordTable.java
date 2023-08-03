package org.example.quizMates.enums;

import lombok.Getter;

@Getter
public enum SessionRecordTable {
    TABLE_NAME("sessionsrecords"),
    SESSION_ID("session_id"),
    PAIR_ID("pair_id"),
    STUDENT_ID("student_id"),
    HOST_ID("host_id"),
    SCORE("score"),
    HOST_NOTES("host_notes"),
    WAS_PRESENT("was_present");

    private final String name;
    SessionRecordTable(String name) {
        this.name = name;
    }
}
