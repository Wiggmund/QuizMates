package org.example.quizMates.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.config.ApplicationConfig;
import org.example.quizMates.dto.pair.CreatePairDto;
import org.example.quizMates.dto.studentsPairs.PairResponse;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Pair;
import org.example.quizMates.service.StudentsPairsService;
import org.example.quizMates.service.impl.StudentsPairsServiceImpl;
import org.example.quizMates.utils.ControllerHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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
        InputStreamReader reader = new InputStreamReader(req.getInputStream());
        Type listType = new TypeToken<List<Long>>() {}.getType();
        List<Long> listGroups = gson.fromJson(reader, listType);
        List<Long> listAbsentStudents = gson.fromJson(reader, listType);
        try {
            PairResponse pairs = studentPairService.getPairsAndUnpairesStudentLists(listGroups, listAbsentStudents);
                    //studentPairService.generatePairs(listGroups, listAbsentStudents);

            ControllerHelper.writeResponse(resp, pairs, HttpServletResponse.SC_OK);

        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            ControllerHelper.writeResponse(resp, exceptionResponse, exceptionResponse.statusCode());
        }
    }
}