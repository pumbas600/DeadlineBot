package net.pumbas.deadlinebot.exceptions;

import net.pumbas.deadlinebot.common.Error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice
{
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error resourceNotFoundHandler(ResourceNotFoundException e) {
        return new Error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error unauthorizedAccessHandler(UnauthorizedAccessException e) {
        return new Error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error badRequestHandler(BadRequestException e) {
        return new Error(e.getMessage());
    }
}
