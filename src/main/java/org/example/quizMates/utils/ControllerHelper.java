package org.example.quizMates.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizMates.config.ApplicationConfig;

import java.io.IOException;
import java.io.PrintWriter;

public class ControllerHelper {
    private static final Gson GSON = ApplicationConfig.GSON;
    public static void writeResponse(HttpServletResponse resp, Object data, int statusCode) throws IOException {
        try(PrintWriter writer =  resp.getWriter()) {
            setResponseHeaders(resp);
            resp.setStatus(statusCode);
            writer.println(GSON.toJson(data));
        }
    }

    public static boolean isParamPresent(String param) {
        return param != null && !param.isEmpty();
    }

    private static void setResponseHeaders(HttpServletResponse resp) {
        resp.setHeader("Content-Type", "application/json");
    }
}
