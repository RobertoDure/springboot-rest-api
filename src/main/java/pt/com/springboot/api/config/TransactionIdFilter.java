package pt.com.springboot.api.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionIdFilter implements Filter {

    public static final String TRANSACTION_ID_KEY = "transactionId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String transactionId = httpServletRequest.getHeader(TRANSACTION_ID_KEY);
        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        // Also add it to MDC so it appears automatically in log messages
        MDC.put("transactionId", transactionId);
        try {
            chain.doFilter(request, response);
            MDC.clear();
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
