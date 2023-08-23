package org.example.quizMates.dto.pair;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CreatePairDto {
    private Long studentA;
    private Long studentB;

    public CreatePairDto(Long studentA, Long studentB) {
        this.studentA = studentA;
        this.studentB = studentB;
    }
}
