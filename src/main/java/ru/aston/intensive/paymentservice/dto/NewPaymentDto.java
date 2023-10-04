package ru.aston.intensive.paymentservice.dto;

import ru.aston.intensive.paymentservice.dao.enums.PaymentType;

import java.math.BigDecimal;

public record NewPaymentDto(Long userId,
                            BigDecimal checkAmount,
                            PaymentType paymentType) {
}
