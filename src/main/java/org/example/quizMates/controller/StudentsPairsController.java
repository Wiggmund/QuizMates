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
import org.example.quizMates.model.Pair;
import org.example.quizMates.service.StudentsPairsService;
import org.example.quizMates.service.impl.StudentsPairsServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/students/pairs")
@RequiredArgsConstructor
public class StudentsPairsController extends HttpServlet {
    private final static Gson gson = ApplicationConfig.GSON;
    private final StudentsPairsService studentPairService;

    public StudentsPairsController() {
        this(StudentsPairsServiceImpl.getInstance());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Pair> pairs = studentPairService.generatePairs();
            ControllerHelper.writeResponse(resp, pairs, HttpServletResponse.SC_OK);

        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}