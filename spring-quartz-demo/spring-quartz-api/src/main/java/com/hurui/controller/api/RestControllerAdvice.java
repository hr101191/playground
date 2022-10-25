package com.hurui.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ResponseStatusException.class })
    protected ResponseEntity<?> handleConflict(ResponseStatusException ex, WebRequest webRequest) {
        logger.info("Exception caught from controller. Stacktrace: ", ex);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getStatus(), webRequest);
    }
}
