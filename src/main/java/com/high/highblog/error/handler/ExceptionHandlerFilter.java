package com.high.highblog.error.handler;

import com.high.highblog.error.exception.InvalidJwtToken;
import com.high.highblog.helper.JsonHelper;
import com.high.highblog.model.dto.response.ErrorMessageRes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter
        extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        ErrorMessageRes errorMessageRes;
        response.setContentType("application/json");

        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJwtToken ex) {
            errorMessageRes = ErrorMessageRes.builder()
                                             .message(ex.getMessage())
                                             .errorCode(ex.getErrorCode())
                                             .fieldName(ex.getFieldName())
                                             .build();

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(JsonHelper.convertObjectToString(errorMessageRes));
        }

    }
}
