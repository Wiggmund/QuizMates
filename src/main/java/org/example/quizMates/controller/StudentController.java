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
import org.example.quizMates.dto.student.UpdateStudentDto;
import org.example.quizMates.exception.ExceptionResponse;
import org.example.quizMates.exception.GlobalExceptionHandler;
import org.example.quizMates.model.Student;
import org.example.quizMates.repository.impl.StudentRepositoryImpl;
import org.example.quizMates.service.StudentService;
import org.example.quizMates.service.impl.StudentServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/students")
@RequiredArgsConstructor
public class StudentController extends HttpServlet {
    private final StudentService studentService;
    private final static Gson gson = new Gson();
    private final static String ID_REQ_PARAM = "id";

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
            String requiredId = req.getParameter(ID_REQ_PARAM);

            if (requiredId == null || requiredId.isEmpty()) {
                List<Student> students = studentService.findAll();
                writer.println(gson.toJson(students));
            } else {
                Student student = studentService.findById(Long.parseLong(requiredId));
                writer.println(gson.toJson(student));
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
            CreateStudentDto dto = gson.fromJson(req.getReader(), CreateStudentDto.class);
            studentService.createStudent(dto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
            writer.println(exceptionResponse.message());
        }
        writer.close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            UpdateStudentDto dto = gson.fromJson(req.getReader(), UpdateStudentDto.class);
            studentService.updateStudent(dto);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = GlobalExceptionHandler.handleException(exception);
            resp.setStatus(exceptionResponse.statusCode());
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
