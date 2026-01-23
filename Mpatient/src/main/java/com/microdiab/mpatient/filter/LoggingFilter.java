package com.microdiab.mpatient.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This filter logs incoming HTTP requests to provide visibility into API activity,
 * including authentication headers, user roles, and request details.
 *
 * The filter captures and logs the following information for each request:
 * <ul>
 *   <li>HTTP method and URI</li>
 *   <li>Authorization header (for authentication)</li>
 *   <li>X-Auth-Username header (username of the authenticated user)</li>
 *   <li>X-Auth-Roles header (roles assigned to the user)</li>
 * </ul>
 *
 * This filter is automatically registered as a Spring component
 * and integrated into the servlet filter chain.
 *
 * @see jakarta.servlet.Filter
 * @see org.springframework.stereotype.Component
 */
@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    /**
     * Processes incoming HTTP requests to log relevant details.
     * This method is called by the servlet container for each request.
     *
     * @param request  The servlet request.
     * @param response The servlet response.
     * @param chain    The filter chain for invoking the next filter or servlet.
     * @throws IOException      If an I/O error occurs during processing.
     * @throws ServletException If a servlet-related error occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String authHeader = httpRequest.getHeader("Authorization");
        String usernameHeader = httpRequest.getHeader("X-Auth-Username");
        String rolesHeader = httpRequest.getHeader("X-Auth-Roles");
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();

        logger.debug("=== Incoming Request to mPatient ===");
        logger.debug("Method: {}, URI: {}", method, uri);
        logger.debug("Authorization Header: {}", authHeader);
        logger.debug("X-Auth-Username: {}", usernameHeader);
        logger.debug("X-Auth-Roles: {}", rolesHeader);

        chain.doFilter(request, response);
    }
}
