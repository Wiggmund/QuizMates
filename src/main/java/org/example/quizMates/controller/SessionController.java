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
import org.example.quizMates.model.Student;
import org.example.quizMates.service.SessionService;
import org.example.quizMates.service.impl.SessionServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.util.List;

import static org.example.quizMates.utils.ControllerHelper.isParamPresent;

@WebServlet("/sessions")
@RequiredArgsConstructor
public class SessionController extends HttpServlet {
    private final SessionService sessionService;
    private final static String ID_REQ_PARAM = "sessionId";
    private final static String HOST_REQ_PARAM = "hostId";
    private final static String STUDENT_ID_REQ_PARAM = "studentId";
    private final static String GROUP_REQ_PARAM = "groupId";
    private final static String SCORE_REQ_PARAM = "score";
    private final static String PRESENT_STUDENTS_REQ_PARAM = "presentStudents";
    private final static String ABSENT_STUDENTS_REQ_PARAM = "absentStudents";
    private final static Gson gson = ApplicationConfig.GSON;

    public SessionController() {
        this(SessionServiceImpl.getInstance());
    }

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String sessionId = req.getParameter(ID_REQ_PARAM);
            String hostId = req.getParameter(HOST_REQ_PARAM);
            String studentId = req.getParameter(STUDENT_ID_REQ_PARAM);
            String groupId = req.getParameter(GROUP_REQ_PARAM);
            String score = req.getParameter(SCORE_REQ_PARAM);
            String presentStudents = req.getParameter(PRESENT_STUDENTS_REQ_PARAM);
            String absentStudents = req.getParameter(ABSENT_STUDENTS_REQ_PARAM);

            if(isParamPresent(sessionId) && isParamPresent(studentId) && isParamPresent(score)) {
                Long studentScoreForSession = sessionService.getStudentScoreForSession(
                        Long.parseLong(studentId), Long.parseLong(sessionId)
                );
                ControllerHelper.writeResponse(resp, studentScoreForSession, HttpServletResponse.SC_OK);
            } else if(isParamPresent(sessionId) && isParamPresent(presentStudents)) {
                List<Student> present = sessionService.getPresentStudents(Long.parseLong(sessionId));
                ControllerHelper.writeResponse(resp, present, HttpServletResponse.SC_OK);
            } else if(isParamPresent(sessionId) && isParamPresent(absentStudents)) {
                List<Student> absent = sessionService.getAbsentStudents(Long.parseLong(sessionId));
                ControllerHelper.writeResponse(resp, absent, HttpServletResponse.SC_OK);
            } else if (isParamPresent(sessionId) && isParamPresent(groupId) && isParamPresent(score)) {
                Long groupScoreForSession = sessionService.getGroupScoreForSession(
                        Long.parseLong(groupId), Long.parseLong(sessionId)
                );
                ControllerHelper.writeResponse(resp, groupScoreForSession, HttpServletResponse.SC_OK);
            } else if (hostId != null && !hostId.isEmpty()) {
                List<Session> hostSessions = sessionService.getHostSessions(Long.parseLong(hostId));
                ControllerHelper.writeResponse(resp, hostSessions, HttpServletResponse.SC_OK);
            } else if (sessionId == null || sessionId.isEmpty()) {
                List<Session> sessions = sessionService.findAll();
                ControllerHelper.writeResponse(resp, sessions, HttpServletResponse.SC_OK);
            } else {
                Session session = sessionService.findById(Long.parseLong(sessionId));
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
            Session session = sessionService.createSession(sessionDto);
            ControllerHelper.writeResponse(resp, session, HttpServletResponse.SC_OK);
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
