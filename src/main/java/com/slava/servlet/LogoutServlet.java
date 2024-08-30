package com.slava.servlet;

import com.slava.dao.SessionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import jakarta.servlet.http.HttpServlet;

public class LogoutServlet extends HttpServlet {
    private SessionDao sessionDao;

    @Override
    public void init() {
        sessionDao = new SessionDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session_token".equals(cookie.getName())) {
                    String sessionToken = cookie.getValue();
                    sessionDao.deleteByToken(sessionToken);

                    cookie.setMaxAge(0);
                    response.addCookie(cookie);

                    break;
                }
            }
        }

        response.sendRedirect("/login");
    }
}
