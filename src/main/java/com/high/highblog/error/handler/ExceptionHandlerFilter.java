package com.high.highblog.error.handler;

import com.high.highblog.constant.AppErrorCode;
import com.high.highblog.error.exception.InvalidJwtToken;
import com.high.highblog.helper.JsonHelper;
import com.high.highblog.model.dto.response.ErrorMessageRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter
        extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        response.setContentType("application/json");
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJwtToken ex) {
            responseError(ex.getMessage(),
                          ex.getErrorCode(),
                          ex.getFieldName(),
                          response,
                          HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            responseError(ex.getMessage(),
                          AppErrorCode.DEFAULT_INTERNAL_SERVER_ERROR,
                          "server",
                          response,
                          HttpStatus.INTERNAL_SERVER_ERROR);
        }
        long endTime = System.currentTimeMillis();

        log.debug("Time to handle request with url #{} is in #{}", request.getPathInfo(), endTime - startTime);
    }

    private void responseError(final String message,
                               final String errorCode,
                               final String fieldName,
                               HttpServletResponse response,
                               HttpStatus httpStatus)
            throws IOException {

        ErrorMessageRes errorMessageRes;
        errorMessageRes = ErrorMessageRes.builder()
                                         .message(message)
                                         .errorCode(errorCode)
                                         .fieldName(fieldName)
                                         .build();
        response.setStatus(httpStatus.value());
        response.getWriter().write(JsonHelper.convertObjectToString(errorMessageRes));
    }
}
