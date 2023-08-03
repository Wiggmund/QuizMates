package org.example.quizMates.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import org.example.quizMates.service.SessionRecordService;

@WebServlet("/sessionsrecords")
@RequiredArgsConstructor
public class SessionRecordController extends HttpServlet {
    private final SessionRecordService sessionRecordService;
    private final static String ID_REQ_PARAM = "id";
    private final static Gson gson = new Gson();

    public SessionRecordController(){

    }
}
