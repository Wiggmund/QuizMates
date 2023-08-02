package org.example.quizMates.service.impl;

import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Host;
import org.example.quizMates.repository.HostRepository;
import org.example.quizMates.repository.impl.HostRepositoryImpl;
import org.example.quizMates.service.DuplicationService;
import org.example.quizMates.service.HostService;

import java.util.List;

public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;
    private final DuplicationService duplicationService;
    private final static String HOST_NOT_FOUND = "Host with id %s not found";
    private final static String HOST_DUPLICATE_NAME = "Host with firstName %s and lastName %s already exists";

    private HostServiceImpl() {
        this.hostRepository = HostRepositoryImpl.getInstance();
        this.duplicationService = DuplicationServiceImpl.getInstance();
    }

    private static class HostServiceSingleton {
        private static final HostService INSTANCE = new HostServiceImpl();
    }

    public static HostService getInstance() {
        return HostServiceSingleton.INSTANCE;
    }

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
        if (duplicationService.doTheSameHostExist(dto.getFirstName(), dto.getLastName())) {
            throw new ResourceNotFoundException(String.format(HOST_DUPLICATE_NAME,
                    dto.getFirstName(), dto.getLastName()));
        }
        hostRepository.createHost(dto);
    }

    @Override
    public void updateHost(UpdateHostDto dto) {
        Host host = findById(dto.getId());

        boolean doFirstNameTheSame = dto.getFirstName().equalsIgnoreCase(host.getFirstName());
        boolean doLastNameTheSame = dto.getLastName().equalsIgnoreCase(host.getLastName());

        if (!doFirstNameTheSame || !doLastNameTheSame) {
            boolean doTheSameHostExist = duplicationService.doTheSameHostExist(dto.getFirstName(), dto.getLastName());

            if (doTheSameHostExist) {
                throw new ResourceNotFoundException(
                        String.format(HOST_DUPLICATE_NAME, dto.getFirstName(), dto.getLastName()));
            }
        }

        hostRepository.updateHost(dto);
    }
}
