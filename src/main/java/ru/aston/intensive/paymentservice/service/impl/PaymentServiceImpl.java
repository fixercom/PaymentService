package ru.aston.intensive.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.repository.PaymentRepository;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;
import ru.aston.intensive.paymentservice.mapper.PaymentMapper;
import ru.aston.intensive.paymentservice.service.PaymentService;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto payOrder(Long orderId, NewPaymentDto newPaymentDto) {
        Payment payment = paymentMapper.toPayment(orderId, newPaymentDto);
        payment.setCreatedDate(LocalDateTime.now());
        PaymentStatus paymentStatus = generatePaymentStatus();
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public Receipt getReceiptByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow();
        return paymentMapper.toReceipt(payment);
    }

    private PaymentStatus generatePaymentStatus() {
        Random random = new Random();
        PaymentStatus paymentStatus = PaymentStatus.REJECTED;
        if (random.nextBoolean()) {
            paymentStatus = PaymentStatus.PAID;
        }
        return paymentStatus;
    }
}
