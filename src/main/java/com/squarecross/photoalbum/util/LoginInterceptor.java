package com.squarecross.photoalbum.util;

import com.squarecross.photoalbum.Constants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(Constants.LOGIN_USER) == null) {
            response.sendRedirect("/users/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
