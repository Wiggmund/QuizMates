package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.dto.session.UpdateSessionDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Session;
import org.example.quizMates.service.SessionService;
import org.example.quizMates.service.impl.SessionServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/sessions")
@RequiredArgsConstructor
public class SessionController extends HttpServlet {
    private final SessionService sessionService;
    private final static String ID_REQ_PARAM = "sessionId";
    private final static String HOST_REQ_PARAM = "hostId";
    private final static Gson gson = ApplicationConfig.GSON;

    public SessionController() {
        this(SessionServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requiredId = req.getParameter(ID_REQ_PARAM);
            String hostId = req.getParameter(HOST_REQ_PARAM);

            if (hostId != null && !hostId.isEmpty()) {
                List<Session> hostSessions = sessionService.getHostSessions(Long.parseLong(hostId));
                ControllerHelper.writeResponse(resp, hostSessions, HttpServletResponse.SC_OK);
            }

            if (requiredId == null || requiredId.isEmpty()) {
                List<Session> sessions = sessionService.findAll();
                ControllerHelper.writeResponse(resp, sessions, HttpServletResponse.SC_OK);
            } else {
                Session session = sessionService.findById(Long.parseLong(requiredId));
                ControllerHelper.writeResponse(resp, session, HttpServletResponse.SC_OK);
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CreateSessionDto sessionDto = gson.fromJson(req.getReader(), CreateSessionDto.class);
            sessionService.createSession(sessionDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UpdateSessionDto dto = gson.fromJson(req.getReader(), UpdateSessionDto.class);
            sessionService.updateSession(dto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long requiredId = Long.parseLong(req.getParameter(ID_REQ_PARAM));
            sessionService.deleteById(requiredId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}
