package com.tahashafiq.contactmanagement.exceptionhandling;
import com.tahashafiq.contactmanagement.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotFoundException(
            ResourceNotFoundException exception) {

        String message = exception.getMessage();

        ApiResponse build = ApiResponse.builder()
                .message(message)
                .success(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(
                build,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();


        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {

                    String fieldName = error.getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.putIfAbsent(
                            fieldName,
                            errorMessage
                    );
                });


        log.warn(
                "Validation failed: {}",
                errors
        );


        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerException(
            Exception exception) {
        log.error(
                "Something went wrong",
                exception
        );

        ApiResponse build = ApiResponse.builder()
                .message("Something went wrong. Please try again later.")
                .success(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();


        return new ResponseEntity<>(
                build,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

