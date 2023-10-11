package ru.aston.intensive.paymentservice.service;

import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;

import java.util.UUID;

public interface PaymentService {

    PaymentDto payOrder(UUID orderId, NewPaymentDto newPaymentDto);

    Receipt getReceiptByOrderId(UUID orderId);

}
