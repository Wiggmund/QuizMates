package org.example.quizMates.dto.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class UpdateStudentDto {
    private Long id;
    private String firstName;
    private String lastName;
}
