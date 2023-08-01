package org.example.quizMates.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.db.PostgreSQLConfig;
import org.example.quizMates.dto.session.CreateSessionDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Session;
import org.example.quizMates.repository.impl.SessionRepositoryImpl;
import org.example.quizMates.service.LocalDateTimeAdapter;
import org.example.quizMates.service.SessionService;
import org.example.quizMates.service.impl.SessionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/sessions")
@RequiredArgsConstructor
public class SessionController extends HttpServlet {
    private final SessionService sessionService;
    private final static String ID_REQ_PARAM = "id";

    public SessionController() {
        this(new SessionServiceImpl(
                new SessionRepositoryImpl(
                        new DBConnectionDriverManager(
                                new PostgreSQLConfig()))));
    }

    private static Gson gsonLocalDateAdapted(){
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String requiredId = req.getParameter(ID_REQ_PARAM);
            if (requiredId == null || requiredId.isEmpty()) {
                List<Session> sessions = sessionService.findAll();
                writer.println(gsonLocalDateAdapted().toJson(sessions));
            } else {
                Session session = sessionService.findById(Long.parseLong(requiredId));
                writer.println(gsonLocalDateAdapted().toJson(session));
            }
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            BufferedReader reader = req.getReader();
            CreateSessionDto sessionDto = gsonLocalDateAdapted().fromJson(reader, CreateSessionDto.class);
            sessionService.createSession(sessionDto);
            writer.println(sessionDto);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            writer.println("Your JSON response");
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            writer.println("Your JSON response");
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }
}
