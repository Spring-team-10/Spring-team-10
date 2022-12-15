package com.sparta.hanghaeboard.exception;

import com.sparta.hanghaeboard.dto.MsgResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(ex.getMessage());

        return new ResponseEntity(
                restApiException,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CustomException.class)
    public MsgResponseDto customeException(final CustomException customException) {
        return new MsgResponseDto(customException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MsgResponseDto processValidationError(MethodArgumentNotValidException ex) {
        return new MsgResponseDto(ex);
    }
}