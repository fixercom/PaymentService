package ru.aston.intensive.paymentservice.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderIsNotPaidException extends RuntimeException {

    private static final String BASE_MESSAGE = "Order with id %s has not been paid";
    private final String message;

    public OrderIsNotPaidException(UUID orderId) {
        message = String.format(BASE_MESSAGE, orderId);
    }

}