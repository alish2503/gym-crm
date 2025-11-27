package com.gymcrm.infrastructure.logging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author Alish
 */
@Component
public class TransactionIdFilter extends OncePerRequestFilter {
    private static final String TX_ID = "transactionId";
    private static final Logger log = LoggerFactory.getLogger(TransactionIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var wrappedRequest = new ContentCachingRequestWrapper(request);
        var wrappedResponse = new ContentCachingResponseWrapper(response);
        String transactionId = UUID.randomUUID().toString();
        MDC.put(TX_ID, transactionId);
        String queryString = wrappedRequest.getQueryString();
        String fullPath = wrappedRequest.getRequestURI() + (queryString != null ? "?" + queryString : "");
        log.info("Incoming request: {} {}", wrappedRequest.getMethod(), fullPath);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logBody(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding(),
                    "Request body");

            logBody(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding(),
                    "Response body");

            log.info("Response status: {}", wrappedResponse.getStatus());
            wrappedResponse.copyBodyToResponse();
            MDC.remove(TX_ID);
        }
    }

    private void logBody(byte[] bodyBytes, String charset, String prefix) throws UnsupportedEncodingException {
        if (bodyBytes.length > 0) {
            String body = new String(bodyBytes, charset).replaceAll("\\s+", " ").
                    replaceAll("(?i)(\"[^\"]*password\"\\s*:\\s*\").*?(\")", "$1***$2").
                    replaceAll("(?i)(\"accessToken\"\\s*:\\s*\").*?(\")", "$1***$2");

            log.info("{}: {}", prefix, body);
        }
    }
}
