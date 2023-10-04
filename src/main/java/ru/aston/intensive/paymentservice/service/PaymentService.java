package ru.aston.intensive.paymentservice.service;

import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;

public interface PaymentService {

    PaymentDto payOrder(Long orderId, NewPaymentDto newPaymentDto);

    Receipt getReceiptByOrderId(Long orderId);

}
