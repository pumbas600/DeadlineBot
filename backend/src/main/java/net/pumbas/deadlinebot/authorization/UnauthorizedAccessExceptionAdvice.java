package net.pumbas.deadlinebot.authorization;

import net.pumbas.deadlinebot.common.Message;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UnauthorizedAccessExceptionAdvice
{
    @ResponseBody
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Message unauthorizedAccessHandler(UnauthorizedAccessException e) {
        return new Message(e.getMessage());
    }
}
