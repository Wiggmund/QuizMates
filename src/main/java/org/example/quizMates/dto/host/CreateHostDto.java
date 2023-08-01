package org.example.quizMates.dto.host;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CreateHostDto {
    private String firstName;
    private String lastName;
}
