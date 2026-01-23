package com.microdiab.mpatient.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

    @InjectMocks
    private LoggingFilter loggingFilter;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletResponse servletResponse;

    @Mock
    private FilterChain filterChain;



    @Test
    void doFilter_shouldLogRequestAndCallFilterChain()
            throws IOException, ServletException {

        // GIVEN
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer test-token");
        when(httpServletRequest.getHeader("X-Auth-Username"))
                .thenReturn("testUser");
        when(httpServletRequest.getHeader("X-Auth-Roles"))
                .thenReturn("ROLE_USER");
        when(httpServletRequest.getMethod())
                .thenReturn("GET");
        when(httpServletRequest.getRequestURI())
                .thenReturn("/api/patient/1");

        // WHEN
        loggingFilter.doFilter(
                httpServletRequest,
                servletResponse,
                filterChain
        );

        // THEN
        verify(httpServletRequest).getHeader("Authorization");
        verify(httpServletRequest).getHeader("X-Auth-Username");
        verify(httpServletRequest).getHeader("X-Auth-Roles");
        verify(httpServletRequest).getMethod();
        verify(httpServletRequest).getRequestURI();

        verify(filterChain).doFilter(
                httpServletRequest,
                servletResponse
        );
    }


    @Test
    void doFilter_shouldWorkWhenHeadersAreMissing()
            throws IOException, ServletException {

        // GIVEN
        when(httpServletRequest.getHeader(anyString()))
                .thenReturn(null);
        when(httpServletRequest.getMethod())
                .thenReturn("POST");
        when(httpServletRequest.getRequestURI())
                .thenReturn("/api/patient");

        // WHEN
        loggingFilter.doFilter(
                httpServletRequest,
                servletResponse,
                filterChain
        );

        // THEN
        verify(filterChain).doFilter(
                httpServletRequest,
                servletResponse
        );
    }
}
