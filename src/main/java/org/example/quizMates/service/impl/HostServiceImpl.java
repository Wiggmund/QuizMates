package org.example.quizMates.service.impl;

import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;
import org.example.quizMates.service.HostService;

import java.util.List;
import java.util.Optional;

public class HostServiceImpl implements HostService {
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
