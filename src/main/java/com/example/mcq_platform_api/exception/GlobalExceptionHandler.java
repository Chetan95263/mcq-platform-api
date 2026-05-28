package com.example.mcq_platform_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.mcq_platform_api.dto.response.MessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class) 
    public ResponseEntity<MessageResponse> handleNotFound(ResourceNotFoundException ex){ 
        return ResponseEntity .status(HttpStatus.NOT_FOUND) .body(new MessageResponse(ex.getMessage())); 
    }
    @ExceptionHandler(BadRequestException.class) 
    public ResponseEntity<MessageResponse> BadhandleRequest(BadRequestException ex){ 
        return ResponseEntity .status(HttpStatus.BAD_REQUEST) .body(new MessageResponse(ex.getMessage())); 
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MessageResponse> handleConflict(ConflictException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new MessageResponse(
                    ex.getMessage()
            ));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new MessageResponse(
                    "Invalid username or password"
            ));
    }
}

