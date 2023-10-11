package ru.aston.intensive.paymentservice.dto;

import ru.aston.intensive.paymentservice.dao.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Receipt(Long id,
                      UUID orderId,
                      BigDecimal checkAmount,
                      PaymentType paymentType,
                      LocalDateTime createdDate) {
}