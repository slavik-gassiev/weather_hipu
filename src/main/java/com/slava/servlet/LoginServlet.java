package com.slava.servlet;

import com.slava.dao.SessionDao;
import com.slava.dao.UserDao;
import com.slava.entity.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

public class LoginServlet extends HttpServlet {
    private UserDao userDao;
    private SessionDao sessionDao;

    @Override
    public void init() {
        userDao = new UserDao();
        sessionDao = new SessionDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        var userOptional = userDao.findByUsername(username);

        if (userOptional.isPresent() && BCrypt.checkpw(password, userOptional.get().getPassword())) {
            String sessionToken = UUID.randomUUID().toString();
            Timestamp expirationTime = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000); // 30 минут

            Session sessionEntity = Session.builder()
                    .userId(userOptional.get().getId())
                    .sessionToken(sessionToken)
                    .expiresAt(expirationTime)
                    .build();

            sessionDao.save(sessionEntity);

            Cookie sessionCookie = new Cookie("session_token", sessionToken);
            sessionCookie.setMaxAge(30 * 60); // 30 минут
            response.addCookie(sessionCookie);

            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/login?error=invalid_credentials");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.html").forward(request, response);
    }
}
