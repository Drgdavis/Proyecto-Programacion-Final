package com.prograiv.importaciones.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiRequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(ApiRequestLoggingFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long inicio = System.nanoTime();
        boolean fallo = false;

        try {
            filterChain.doFilter(request, response);

        } catch (ServletException | IOException | RuntimeException exception) {
            fallo = true;

            log.error(
                    "Fallo HTTP metodo={} ruta={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    exception
            );

            throw exception;

        } finally {
            if (!fallo) {
                long duracionMs =
                        (System.nanoTime() - inicio) / 1_000_000;

                if (response.getStatus() >= 400) {
                    log.warn(
                            "HTTP metodo={} ruta={} estado={} duracionMs={}",
                            request.getMethod(),
                            request.getRequestURI(),
                            response.getStatus(),
                            duracionMs
                    );
                } else {
                    log.info(
                            "HTTP metodo={} ruta={} estado={} duracionMs={}",
                            request.getMethod(),
                            request.getRequestURI(),
                            response.getStatus(),
                            duracionMs
                    );
                }
            }
        }
    }
}