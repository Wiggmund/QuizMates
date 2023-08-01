package org.example.quizMates.repository;

import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;

public interface HostRepository extends CrudRepository<Host, Long> {
    void createHost(CreateHostDto dto);
    void updateHost(UpdateHostDto dto);
}
