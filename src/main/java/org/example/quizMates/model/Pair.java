package org.example.quizMates.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
public class Pair {
    private Long id;
    private Long studentA;
    private Long studentB;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(id, pair.id) &&
                (Objects.equals(studentA, pair.studentA) && Objects.equals(studentB, pair.studentB))
                ||
                (Objects.equals(studentA, pair.studentB) && Objects.equals(studentB, pair.studentA));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentA, studentB);
    }
}
