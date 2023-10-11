package ru.aston.intensive.paymentservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDto(UUID orderId,
                         Boolean isPaid,
                         LocalDateTime createdDate) {

}