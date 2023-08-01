package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;
import org.example.quizMates.repository.HostRepository;
import org.example.quizMates.service.HostService;

import java.util.List;

@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;

    @Override
    public Host findById(Long aLong) {
        return null;
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
