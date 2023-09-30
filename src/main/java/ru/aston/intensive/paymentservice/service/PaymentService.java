package ru.aston.intensive.paymentservice.service;

import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;

public interface PaymentService {

    PaymentDto payOrder(Long orderId, Long userId);

    Receipt getReceiptByOrderId(Long orderId, Long userId);

}
