package org.example.quizMates.dto.pair;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
public class CreatePairDto {
    private Long studentA;
    private Long studentB;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatePairDto that = (CreatePairDto) o;
        return (Objects.equals(studentA, that.studentA) && Objects.equals(studentB, that.studentB))
                ||
                (Objects.equals(studentA, that.studentB) && Objects.equals(studentB, that.studentA));
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentA, studentB);
    }
}
