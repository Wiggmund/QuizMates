package org.example.quizMates.repository;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;

public interface PairRepository extends CrudRepository<Pair, Long> {
    void createPair(CreatePairDto dto);
    void updatePair(UpdatePairDto dto);
}
