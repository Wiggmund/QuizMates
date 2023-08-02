package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.PairRepository;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.PairService;

import java.util.List;

@RequiredArgsConstructor
public class PairServiceImpl implements PairService {
    private final PairRepository pairRepository;
    private final DuplicationService duplicationService;

    @Override
    public Pair findById(Long aLong) {
        return null;
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
