package com.high.highblog.error.handler;

import com.high.highblog.constant.AppErrorCode;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.dto.response.ErrorMessageRes;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.text.ParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ErrorMessageRes> handleRuntimeException(ValidatorException ex) {
        ErrorMessageRes errorMessageRes = ErrorMessageRes.builder()
                                                         .message(ex.getMessage())
                                                         .fieldName(ex.getFieldName())
                                                         .errorCode(ex.getErrorCode())
                                                         .build();

        return ResponseEntity.badRequest().body(errorMessageRes);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorMessageRes errorMessageRes = null;

        Throwable throwable = ex.getMostSpecificCause();
        boolean isEnumArgument = throwable.getMessage().contains("enums");

        if (throwable instanceof ParseException) {

            errorMessageRes = ErrorMessageRes.builder()
                                             .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                             .message(ex.getMessage())
                                             .fieldName(ex.getName())
                                             .build();

        } else if (throwable instanceof IllegalArgumentException && isEnumArgument) {

            errorMessageRes = ErrorMessageRes.builder()
                                             .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                             .message(ex.getMessage())
                                             .fieldName(ex.getName())
                                             .build();
        } else {

            errorMessageRes = ErrorMessageRes.builder()
                                             .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                             .message(ex.getMessage())
                                             .fieldName(ex.getName())
                                             .build();
        }
        return ResponseEntity.badRequest().body(errorMessageRes);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> authenticationException(BadCredentialsException ex) {
        ErrorMessageRes errorMessageRes = ErrorMessageRes.builder()
                                                         .message("Invalid login info")
                                                         .errorCode(AppErrorCode.INVALID_LOGIN_INFO)
                                                         .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessageRes);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex) {

        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        String field = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = violation.getMessage();

        ErrorMessageRes errorMessageRes = ErrorMessageRes.builder()
                                                         .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                                         .message(message)
                                                         .fieldName(field)
                                                         .build();

        return ResponseEntity.badRequest().body(errorMessageRes);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<?> propertyReferenceException(PropertyReferenceException ex) {
        ErrorMessageRes errorMessageRes = ErrorMessageRes.builder()
                                                         .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                                         .fieldName(ex.getPropertyName())
                                                         .message(ex.getMessage())
                                                         .build();
        return ResponseEntity.badRequest().body(errorMessageRes);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<?> missingServletRequestPartException(ServletException ex) {
        ErrorMessageRes errorMessageRes = ErrorMessageRes.builder()
                                                         .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                                         .message(ex.getMessage())
                                                         .fieldName("servlet")
                                                         .build();

        return ResponseEntity.badRequest().body(errorMessageRes);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorMessageRes errorMessageRes = ErrorMessageRes.builder()
                                                         .errorCode(AppErrorCode.DEFAULT_VALIDATOR)
                                                         .message(ex.getAllErrors().get(0).getDefaultMessage())
                                                         .fieldName(((FieldError) ex.getAllErrors().get(0)).getField())
                                                         .build();

        return ResponseEntity.badRequest().body(errorMessageRes);
    }
}
