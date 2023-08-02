package org.example.quizMates.enums;

import lombok.Getter;

@Getter
public enum HostTable {
    TABLE_NAME("hosts"),
    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name");

    private final String name;

    HostTable(String name) {
        this.name = name;
    }
}
