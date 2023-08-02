package org.example.quizMates.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private Long groupId;
}
