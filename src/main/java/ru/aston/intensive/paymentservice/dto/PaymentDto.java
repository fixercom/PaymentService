package ru.aston.intensive.paymentservice.dto;

import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentDto(Long id,
                         Long orderId,
                         PaymentStatus paymentStatus,
                         LocalDateTime createdDate) {
}