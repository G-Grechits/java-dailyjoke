package ru.grechits.dailyjoke.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApiError {
    private List<Error> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
