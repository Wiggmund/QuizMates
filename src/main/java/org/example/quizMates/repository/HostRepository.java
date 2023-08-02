package org.example.quizMates.repository;

import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;

import java.util.Optional;

public interface HostRepository extends CrudRepository<Host, Long> {
    Optional<Host> findByFirstNameAndLastName(String firstName, String lastName);
    void createHost(CreateHostDto dto);
    void updateHost(UpdateHostDto dto);
}
