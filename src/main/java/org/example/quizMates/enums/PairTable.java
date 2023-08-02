package org.example.quizMates.enums;

import lombok.Getter;

@Getter
public enum PairTable {
    TABLE_NAME("pairs"),
    ID("id"),
    STUDENT_A("student_a"),
    STUDENT_B("student_b");

    private final String name;

    PairTable(String name) {
        this.name = name;
    }
}
