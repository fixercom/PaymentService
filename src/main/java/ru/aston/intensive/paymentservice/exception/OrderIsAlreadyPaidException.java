package ru.aston.intensive.paymentservice.exception;

import lombok.Getter;

@Getter
public class OrderIsAlreadyPaidException extends RuntimeException {

    private static final String BASE_MESSAGE = "Order with id %d has already been paid for";
    private final String message;

    public OrderIsAlreadyPaidException(Long orderId) {
        message = String.format(BASE_MESSAGE, orderId);
    }

}