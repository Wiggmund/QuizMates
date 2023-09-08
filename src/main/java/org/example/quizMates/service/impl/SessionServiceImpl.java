package org.example.quizMates.service.impl;

import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.exception.ResourceNotFoundException;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.SessionRepository;
import org.example.quizMates.repository.impl.SessionRepositoryImpl;
import org.example.quizMates.service.*;

import java.util.List;
import java.util.Optional;

public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final StudentService studentService;
    private final HostService hostService;
    private final GroupService groupService;
    private final DuplicationService duplicationService;
    private final static String SESSION_NOT_FOUND = "Session with id %s not found";
    private final static String SESSION_DUPLICATE_TITLE = "Session with title %s already exists";


    private SessionServiceImpl() {
        this.sessionRepository = SessionRepositoryImpl.getInstance();
        this.duplicationService = DuplicationServiceImpl.getInstance();
        this.studentService = StudentServiceImpl.getInstance();
        this.groupService = GroupServiceImpl.gteInstance();
        this.hostService = HostServiceImpl.getInstance();
    }

    private static class SessionServiceSingleton {
        private static final SessionService INSTANCE = new SessionServiceImpl();
    }

    public static SessionService getInstance() {
        return SessionServiceSingleton.INSTANCE;
    }

    @Override
    public Session findById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(SESSION_NOT_FOUND, id)));
    }

    @Override
    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public List<Session> getHostSessions(Long hostId) {
        hostService.findById(hostId);
        return sessionRepository.getHostSessions(hostId);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        sessionRepository.deleteById(id);
    }

    @Override
    public Optional<Session> getLastSession() {
        return sessionRepository.getLastSession();
    }

    @Override
    public Long createSession(CreateSessionDto dto) {
        if (duplicationService.doTheSameSessionExist(dto.getTitle())) {
            throw new ResourceNotFoundException(String.format(SESSION_DUPLICATE_TITLE,
                    dto.getTitle()));
        }
        return sessionRepository.createSession(dto);
    }

    @Override
    public void updateSession(UpdateSessionDto dto) {
        Session session = findById(dto.getId());

        if (dto.getBestStudent() != null) {
            studentService.findById(dto.getBestStudent());
        }
        if (dto.getBestGroup() != null) {
            groupService.findById(dto.getBestGroup());
        }

        boolean doTitleTheSame = dto.getTitle().equalsIgnoreCase(session.getTitle());

        if (!doTitleTheSame) {
            boolean doTheSameSessionExist = duplicationService.doTheSameSessionExist(dto.getTitle());

            if (doTheSameSessionExist) {
                throw new ResourceNotFoundException(
                        String.format(SESSION_DUPLICATE_TITLE, dto.getTitle()));
            }
        }
        sessionRepository.updateSession(dto);
    }
}
