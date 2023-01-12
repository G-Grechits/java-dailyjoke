package ru.grechits.dailyjoke.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static ru.grechits.dailyjoke.mapper.DateTimeMapper.toTextDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class,
            MissingServletRequestParameterException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final Exception e) {
        log.error("Error 400 has occurred: {}", e.getMessage(), e);

        return ApiError.builder()
                .message(e.getMessage())
                .reason("The conditions for performing the operation are not met.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(toTextDateTime(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error("Error 404 has occurred: {}", e.getMessage(), e);

        return ApiError.builder()
                .message(e.getMessage())
                .reason("The requested object was not found.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(toTextDateTime(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error("An unexpected error has occurred: {}", e.getMessage(), e);

        return ApiError.builder()
                .message(e.getMessage())
                .reason("Internal server error.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(toTextDateTime(LocalDateTime.now()))
                .build();
    }
}
