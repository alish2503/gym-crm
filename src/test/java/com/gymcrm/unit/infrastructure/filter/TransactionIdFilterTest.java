package com.gymcrm.unit.infrastructure.filter;

import com.gymcrm.infrastructure.logging.filter.TransactionIdFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TransactionIdFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TransactionIdFilter filter;

    @Test
    void shouldHandleRequestWithQueryString() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("POST");
        when(request.getQueryString()).thenReturn("param=1");
        filter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(any(), any());
        assertNull(MDC.get("transactionId"));
    }

    @Test
    void shouldHandleExceptionAndStillClearMDC() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getQueryString()).thenReturn(null);
        doThrow(new ServletException("Chain error")).when(filterChain).doFilter(any(), any());
        assertThrows(ServletException.class, () -> filter.doFilter(request, response, filterChain));
        assertNull(MDC.get("transactionId"));
    }
}

