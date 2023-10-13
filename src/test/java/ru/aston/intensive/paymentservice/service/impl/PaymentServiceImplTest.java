package ru.aston.intensive.paymentservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Spy
    private PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private RandomGenerator randomGenerator;
    @InjectMocks
    private PaymentServiceImpl paymentService;


    @Test
    void payOrder() {
        UUID uuid = UUID.randomUUID();
        Payment payment = new Payment(1L, uuid, new BigDecimal(10), PaymentStatus.REJECTED, PaymentType.DEBIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        NewPaymentDto newPaymentDto = new NewPaymentDto(new BigDecimal(10));

        Mockito.when(paymentRepository.findByOrderId(uuid)).thenReturn(Optional.of(payment));
        Mockito.when(paymentMapper.updatePayment(any(), any())).thenReturn(payment);
        Mockito.when(randomGenerator.nextBoolean()).thenReturn(false);
        Mockito.when(randomGenerator.nextInt(0, 4)).thenReturn(1);


        PaymentDto checkDto = paymentService.payOrder(uuid, newPaymentDto);
        assertAll(
                () -> assertEquals(uuid, checkDto.orderId()),
                () -> assertEquals(LocalDateTime.parse("2023-10-06T01:16:56"), checkDto.createdDate())
        );

        Mockito.verify(paymentMapper).toPayment(any(UUID.class), any(NewPaymentDto.class));
        Mockito.verify(paymentMapper).toPaymentDto(any(Payment.class));
    }

    @Test
    void payOrder_whenOrderIsAlreadyPaid_thenThrowOrderIsAlreadyPaidException() {
        UUID uuid = UUID.randomUUID();
        Payment payment = new Payment(1L, uuid, new BigDecimal(10), PaymentStatus.PAID, PaymentType.DEBIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        NewPaymentDto newPaymentDto = new NewPaymentDto(new BigDecimal(10));
        Mockito.when(paymentRepository.findByOrderId(uuid)).thenReturn(Optional.of(payment));

        assertThrows(OrderIsAlreadyPaidException.class, () -> paymentService.payOrder(uuid, newPaymentDto));
    }

    @Test
    void getReceiptByOrderId_whenOrderIsNotPaid_thenThrowOrderIsNotPaidException() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(paymentRepository.findByOrderIdAndPaymentStatus(uuid, PaymentStatus.PAID)).thenReturn(Optional.empty());

        assertThrows(OrderIsNotPaidException.class, () -> paymentService.getReceiptByOrderId(uuid));
    }

    @Test
    void getReceiptByOrderId() {
        UUID uuid = UUID.randomUUID();
        Payment payment = new Payment(1L, uuid, new BigDecimal(10), PaymentStatus.REJECTED, PaymentType.DEBIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        Receipt receipt = new Receipt(1L, uuid, new BigDecimal(10), PaymentType.DEBIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));

        Mockito.when(paymentRepository.findByOrderIdAndPaymentStatus(uuid, PaymentStatus.PAID)).thenReturn(Optional.of(payment));
        Mockito.when(paymentMapper.toReceipt(payment)).thenReturn(receipt);

        Receipt receiptByOrderId = paymentService.getReceiptByOrderId(uuid);

        assertAll(
                () -> assertEquals(1L, receiptByOrderId.id()),
                () -> assertEquals(uuid, receiptByOrderId.orderId()),
                () -> assertEquals(PaymentType.DEBIT_CARD, receiptByOrderId.paymentType()),
                () -> assertEquals(LocalDateTime.parse("2023-10-06T01:16:56"), receiptByOrderId.createdDate())
        );
    }
}