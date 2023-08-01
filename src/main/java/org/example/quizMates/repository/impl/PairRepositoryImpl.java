package org.example.quizMates.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnection;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.pair.UpdatePairDto;
import org.example.quizMates.model.Pair;
import org.example.quizMates.repository.PairRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PairRepositoryImpl implements PairRepository {
    private final DBConnection dbConnection;

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
