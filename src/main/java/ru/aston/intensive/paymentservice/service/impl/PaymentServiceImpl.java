package ru.aston.intensive.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;
import ru.aston.intensive.paymentservice.dao.repository.PaymentRepository;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;
import ru.aston.intensive.paymentservice.exception.OrderIsAlreadyPaidException;
import ru.aston.intensive.paymentservice.exception.OrderIsNotPaidException;
import ru.aston.intensive.paymentservice.mapper.PaymentMapper;
import ru.aston.intensive.paymentservice.service.PaymentService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.random.RandomGenerator;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final RandomGenerator randomGenerator;

    @Override
    @Transactional
    public PaymentDto payOrder(Long orderId, NewPaymentDto newPaymentDto) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        Payment payment = createPayment(orderId, newPaymentDto);
        if (paymentOptional.isPresent()) {
            Payment loadedPayment = paymentOptional.get();
            if (loadedPayment.getPaymentStatus() == PaymentStatus.PAID) {
                throw new OrderIsAlreadyPaidException(loadedPayment.getOrderId());
            }
            Payment updatedPayment = paymentMapper.updatePayment(payment, loadedPayment);
            return paymentMapper.toPaymentDto(updatedPayment);
        }
        paymentRepository.save(payment);
        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public Receipt getReceiptByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderIdAndPaymentStatus(orderId, PaymentStatus.PAID)
                .orElseThrow(() -> new OrderIsNotPaidException(orderId));
        return paymentMapper.toReceipt(payment);
    }

    private Payment createPayment(Long orderId, NewPaymentDto newPaymentDto) {
        Payment payment = paymentMapper.toPayment(orderId, newPaymentDto);
        payment.setCreatedDate(LocalDateTime.now());
        payment.setPaymentStatus(generatePaymentStatus());
        payment.setPaymentType(generatePaymentType());
        return payment;
    }

    private PaymentStatus generatePaymentStatus() {
        PaymentStatus paymentStatus = PaymentStatus.REJECTED;
        if (randomGenerator.nextBoolean()) {
            paymentStatus = PaymentStatus.PAID;
        }
        return paymentStatus;
    }

    private PaymentType generatePaymentType() {
        return PaymentType.values()[randomGenerator.nextInt(0, 4)];
    }

}
