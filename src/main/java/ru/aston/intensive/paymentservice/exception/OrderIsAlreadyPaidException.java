package ru.aston.intensive.paymentservice.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderIsAlreadyPaidException extends RuntimeException {

    private static final String BASE_MESSAGE = "Order with id %s has already been paid for";
    private final String message;

    public OrderIsAlreadyPaidException(UUID orderId) {
        message = String.format(BASE_MESSAGE, orderId);
    }

}