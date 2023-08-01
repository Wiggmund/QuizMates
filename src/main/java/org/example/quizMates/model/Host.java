package org.example.quizMates.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class Host {
    private Long id;
    private String firstName;
    private String lastName;
}
