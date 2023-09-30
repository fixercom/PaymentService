package ru.aston.intensive.paymentservice.dto;

import ru.aston.intensive.paymentservice.dao.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Receipt(Long id,
                      Long orderId,
                      BigDecimal checkAmount,
                      PaymentType paymentType,
                      LocalDateTime createdDate) {
}