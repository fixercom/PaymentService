package ru.aston.intensive.paymentservice.dto;

import java.math.BigDecimal;

public record NewPaymentDto(BigDecimal totalPrice) {

}
