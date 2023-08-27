package org.example.quizMates.enums;

import lombok.Getter;

@Getter
public enum GroupTable {
    TABLE_NAME("groups"),
    ID("id"),
    NAME("name"),
    STUDENTS_AMOUNT("students_amount"),
    TEAMLEAD_ID("teamlead_Id");


    private final String name;

    GroupTable(String name) {
        this.name = name;
    }
}
