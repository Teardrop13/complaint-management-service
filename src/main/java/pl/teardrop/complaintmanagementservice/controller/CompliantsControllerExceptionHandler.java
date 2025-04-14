package pl.teardrop.complaintmanagementservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.teardrop.complaintmanagementservice.exceptions.InvalidArgumentException;
import pl.teardrop.complaintmanagementservice.exceptions.NotFoundException;

@ControllerAdvice
@Slf4j
public class CompliantsControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            NotFoundException.class
    })
    ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
        log.error("Exception was thrown after receiving request", ex);
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            InvalidArgumentException.class
    })
    ResponseEntity<Object> handleNotFound(InvalidArgumentException ex, WebRequest request) {
        log.error("Exception was thrown after receiving request", ex);
        return super.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}