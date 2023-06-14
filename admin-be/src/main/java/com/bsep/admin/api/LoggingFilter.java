package com.bsep.admin.api;

import com.bsep.admin.model.LogType;
import com.bsep.admin.service.LogService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class LoggingFilter implements Filter {
    private final LogService logService;

    public LoggingFilter(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String logDetails = "HTTP Method: " + req.getMethod() + ", URI: " + req.getRequestURI();
        logService.logAction(LogType.INFO, "HTTP Request", logDetails);
        chain.doFilter(request, response);
    }
}