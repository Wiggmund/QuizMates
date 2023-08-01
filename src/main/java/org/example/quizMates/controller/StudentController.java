package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.db.DBConnectionDriverManager;
import org.example.quizMates.db.PostgreSQLConfig;
import org.example.quizMates.dto.student.CreateStudentDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.repository.impl.StudentRepositoryImpl;
import org.example.quizMates.service.StudentService;
import org.example.quizMates.service.impl.StudentServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/students")
@RequiredArgsConstructor
public class StudentController extends HttpServlet {
    private final StudentService studentService;
    private final static Gson gson = new Gson();

    public StudentController() {
        this(new StudentServiceImpl(
                new StudentRepositoryImpl(
                        new DBConnectionDriverManager(
                                new PostgreSQLConfig()))));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            CreateStudentDto dto = gson.fromJson(req.getReader(), CreateStudentDto.class);
            studentService.createStudent(dto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
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
