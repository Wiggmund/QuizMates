package org.example.quizMates.enums;

import lombok.Getter;

@Getter
public enum StudentTable {
    TABLE_NAME("students"),
    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    GROUP_ID("group_id");

    private final String name;

    StudentTable(String name) {
        this.name = name;
    }
}
