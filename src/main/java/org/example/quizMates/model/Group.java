package org.example.quizMates.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class Group {
    private Long id;
    private String name;
    private Integer studentsAmount;
    private Long teamleadId;
}
