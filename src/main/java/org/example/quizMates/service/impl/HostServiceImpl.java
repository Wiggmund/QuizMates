package org.example.quizMates.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Host;
import org.example.quizMates.repository.HostRepository;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.HostService;

import java.util.List;

@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;
    private final DuplicationService duplicationService;
    private final static String HOST_NOT_FOUND = "Host with id %s not found";

    @Override
    public Host findById(Long id) {
        return hostRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(HOST_NOT_FOUND, id)));
    }

    @Override
    public List<Host> findAll() {
        return hostRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        hostRepository.deleteById(id);
    }

    @Override
    public void createHost(CreateHostDto dto) {
        hostRepository.createHost(dto);
    }

    @Override
    public void updateHost(UpdateHostDto dto) {
        findById(dto.getId());
        hostRepository.updateHost(dto);
    }
}
