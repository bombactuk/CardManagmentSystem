package com.bank.cardManagementSystem.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        try {
            logRequest(wrappedRequest);

            chain.doFilter(wrappedRequest, wrappedResponse);

            logResponse(wrappedResponse);
        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        Map<String, String> headers = getHeaders(request);

        log.info(
                "REQUEST:\n" +
                        "Method: {}\n" +
                        "URI: {}\n" +
                        "Headers: {}\n" +
                        "Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                headers,
                new String(request.getContentAsByteArray(), request.getCharacterEncoding())
        );
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        Map<String, String> headers = new HashMap<>();
        response.getHeaderNames().forEach(headerName ->
                headers.put(headerName, response.getHeader(headerName))
        );

        log.info(
                "RESPONSE:\n" +
                        "Status: {}\n" +
                        "Headers: {}\n" +
                        "Body: {}",
                response.getStatus(),
                headers,
                new String(response.getContentAsByteArray(), response.getCharacterEncoding())
        );
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

}
