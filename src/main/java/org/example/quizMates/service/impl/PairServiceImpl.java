package org.example.quizMates.service.impl;

import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;
import org.example.quizMates.service.PairService;

import java.util.List;
import java.util.Optional;

public class PairServiceImpl implements PairService {
    @Override
    public Optional<Pair> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Pair> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createPair(CreatePairDto dto) {

    }

    @Override
    public void updatePair(UpdatePairDto dto) {

    }
}
