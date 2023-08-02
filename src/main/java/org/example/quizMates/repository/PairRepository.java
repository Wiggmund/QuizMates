package org.example.quizMates.repository;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;

import java.util.Optional;

public interface PairRepository extends CrudRepository<Pair, Long> {
    Optional<Pair> findByStudentAAndStudentB(Long studentA, Long studentB);
    void createPair(CreatePairDto dto);
    void updatePair(UpdatePairDto dto);
}
