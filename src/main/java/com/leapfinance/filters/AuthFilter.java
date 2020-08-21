package com.leapfinance.filters;

import com.leapfinance.JWTUtil;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String token = req.getHeader("Authorization");
        if(token == null) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "token is not provided");
            return;
        }
        try {
            JWTUtil.validateToken(token);
            req.setAttribute("username", JWTUtil.extractSubject(token));
        }
        catch (Exception ex) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "invalid/expired token provided");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
