package org.example.quizMates.service;

import org.example.quizMates.dto.host.CreateHostDto;
import org.example.quizMates.dto.host.UpdateHostDto;
import org.example.quizMates.model.Host;

public interface HostService extends CrudService<Host, Long> {
    void createHost(CreateHostDto dto);
    void updateHost(UpdateHostDto dto);
}
