package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;
import org.example.quizMates.repository.HostRepository;
import org.example.quizMates.service.HostService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;

    @Override
    public Optional<Host> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Host> findAll() {
        return hostRepository.findAll();
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
