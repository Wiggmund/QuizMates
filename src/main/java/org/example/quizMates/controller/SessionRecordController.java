package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.dto.sessionrecord.CreateSessionRecordDto;
import org.example.quizMates.dto.sessionrecord.UpdateSessionRecordDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.SessionRecord;
import org.example.quizMates.service.SessionRecordService;
import org.example.quizMates.service.impl.SessionRecordServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/sessionsrecords")
@RequiredArgsConstructor
public class SessionRecordController extends HttpServlet {
    private final SessionRecordService sessionRecordService;
    private final static String ID_REQ_PARAM = "id";
    private final static String STUDENT_ID_REQ_PARAM = "studentId";
    private final static String SESSION_ID_REQ_PARAM = "sessionId";
    private final static Gson gson = new Gson();

    public SessionRecordController() {
        this(SessionRecordServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String requiredId = req.getParameter(ID_REQ_PARAM);
            String studentId = req.getParameter(STUDENT_ID_REQ_PARAM);
            String sessionId = req.getParameter(SESSION_ID_REQ_PARAM);

            if (requiredId != null && !requiredId.isEmpty()) {
                SessionRecord sessionRecord = sessionRecordService.findById(Long.parseLong(requiredId));
                ControllerHelper.writeResponse(resp, sessionRecord, HttpServletResponse.SC_OK);
            } else if (studentId != null && !studentId.isEmpty()) {
                List<SessionRecord> sessionRecords = sessionRecordService.findByStudentId(Long.parseLong(studentId));
                ControllerHelper.writeResponse(resp, sessionRecords, HttpServletResponse.SC_OK);
            } else if (sessionId != null && !sessionId.isEmpty()) {
                List<SessionRecord> sessionRecords = sessionRecordService.findBySessionId(Long.parseLong(sessionId));
                ControllerHelper.writeResponse(resp, sessionRecords, HttpServletResponse.SC_OK);
            } else {
                List<SessionRecord> sessionRecords = sessionRecordService.findAll();
                ControllerHelper.writeResponse(resp, sessionRecords, HttpServletResponse.SC_OK);
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            CreateSessionRecordDto sessionRecordDto = gson.fromJson(req.getReader(), CreateSessionRecordDto.class);
            sessionRecordService.createSessionRecord(sessionRecordDto);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UpdateSessionRecordDto dto = gson.fromJson(req.getReader(), UpdateSessionRecordDto.class);
            sessionRecordService.updateSessionRecord(dto);
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
            sessionRecordService.deleteById(requiredId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}
