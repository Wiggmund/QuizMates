package org.example.quizMates.repository.impl;

import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;
import org.example.quizMates.repository.HostRepository;

import java.util.List;
import java.util.Optional;

public class HostRepositoryImpl implements HostRepository {
    @Override
    public Optional<Host> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Host> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void createHost(CreateHostDto dto) {

    }

    @Override
    public void updateHost(UpdateHostDto dto) {

    }
}
