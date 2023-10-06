package ru.aston.intensive.paymentservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.aston.intensive.paymentservice.exception.OrderIsAlreadyPaidException;
import ru.aston.intensive.paymentservice.exception.OrderIsNotPaidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderIsAlreadyPaidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleOrderIsAlreadyPaidException(OrderIsAlreadyPaidException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(OrderIsNotPaidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOrderIsNotPaidException(OrderIsNotPaidException exception) {
        return exception.getMessage();
    }

}
