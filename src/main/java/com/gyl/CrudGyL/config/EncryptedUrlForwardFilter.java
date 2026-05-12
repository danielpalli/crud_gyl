package com.gyl.CrudGyL.config;

import com.gyl.CrudGyL.service.UrlEncryptionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class EncryptedUrlForwardFilter extends OncePerRequestFilter {
    private static final String CRYPTO_ENDPOINT_PREFIX = "/api/url-crypto";

    private final UrlEncryptionService urlEncryptionService;
    private final Environment environment;

    public EncryptedUrlForwardFilter(UrlEncryptionService urlEncryptionService, Environment environment) {
        this.urlEncryptionService = urlEncryptionService;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        if (!isProdProfileActive()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestUri.startsWith(CRYPTO_ENDPOINT_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestUri.startsWith("/api/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("/".equals(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String forwardPath = urlEncryptionService.decryptRequestPath(requestUri);
            request.getRequestDispatcher(forwardPath).forward(request, response);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private boolean isProdProfileActive() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch("prod"::equalsIgnoreCase);
    }
}
