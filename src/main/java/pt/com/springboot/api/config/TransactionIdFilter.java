package pt.com.springboot.api.config;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

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
        // Store transactionId in our custom ThreadLocal context holder
        TransactionContextHolder.setTransactionId(transactionId);
        try {
            chain.doFilter(request, response);
        } finally {
            TransactionContextHolder.clear();
        }
    }

    @Override
    public void destroy() {
      // Do nothing
    }
}
