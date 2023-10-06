package ru.aston.intensive.paymentservice.exception;

import lombok.Getter;

@Getter
public class OrderIsNotPaidException extends RuntimeException {

    private static final String BASE_MESSAGE = "Order with id %d has not been paid";
    private final String message;

    public OrderIsNotPaidException(Long orderId) {
        message = String.format(BASE_MESSAGE, orderId);
    }

}