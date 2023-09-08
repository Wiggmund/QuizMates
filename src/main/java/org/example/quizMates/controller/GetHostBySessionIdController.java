package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Session;
import org.example.quizMates.service.SessionRecordService;
import org.example.quizMates.service.SessionService;
import org.example.quizMates.service.impl.SessionRecordServiceImpl;
import org.example.quizMates.service.impl.SessionServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/session")
@RequiredArgsConstructor
public class GetHostBySessionIdController extends HttpServlet {

    //private final SessionService sessionService;
    private final SessionRecordService sessionRecordService;
    private final static String ID_REQ_PARAM = "sessionId";
    private final static Gson gson = ApplicationConfig.GSON;

    public GetHostBySessionIdController() {
        this(SessionRecordServiceImpl.getInstance());
                //SessionServiceImpl.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String sessionId = req.getParameter(ID_REQ_PARAM);
            String host = req.getParameter("host");
            if (sessionId != null && !sessionId.isEmpty() && Boolean.parseBoolean(host)) {
                Long neededHostId = sessionRecordService.findByIdAndGetHostId(Long.parseLong(sessionId));
                ControllerHelper.writeResponse(resp, neededHostId, HttpServletResponse.SC_OK);
            }
//            } else {
//                Session session = sessionService.findById(Long.parseLong(requiredId));
//                ControllerHelper.writeResponse(resp, session, HttpServletResponse.SC_OK);
//            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }

    }
}
