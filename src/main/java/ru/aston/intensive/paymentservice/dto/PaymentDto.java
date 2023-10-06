package ru.aston.intensive.paymentservice.dto;

import java.time.LocalDateTime;

public record PaymentDto(Long orderId,
                         Boolean isPaid,
                         LocalDateTime createdDate) {

}