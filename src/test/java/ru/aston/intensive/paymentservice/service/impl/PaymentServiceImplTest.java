package ru.aston.intensive.paymentservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;
import ru.aston.intensive.paymentservice.dao.repository.PaymentRepository;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;
import ru.aston.intensive.paymentservice.mapper.PaymentMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RandomGenerator randomGenerator;

    @InjectMocks
    private PaymentServiceImpl paymentService;



    @Test
    void payOrder() {
        Payment payment = new Payment(1L,1L,new BigDecimal(10), PaymentStatus.REJECTED, PaymentType.DEBIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        PaymentDto paymentDto = new PaymentDto(1L,true,LocalDateTime.parse("2023-10-06T01:16:56"));
        NewPaymentDto newPaymentDto = new NewPaymentDto(new BigDecimal(10));

        Mockito.when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment));
        Mockito.when(paymentMapper.toPayment(1L,newPaymentDto)).thenReturn(payment);
        Mockito.when(paymentMapper.toPaymentDto(payment)).thenReturn(paymentDto);
        Mockito.when(paymentMapper.updatePayment(any(),any())).thenReturn(payment);
        Mockito.when(randomGenerator.nextBoolean()).thenReturn(false);
        Mockito.when(randomGenerator.nextInt(0,4)).thenReturn(1);


        PaymentDto checkDto = paymentService.payOrder(1L, newPaymentDto);
        assertAll(
                () -> assertEquals(1L,checkDto.orderId()),
                () -> assertTrue(checkDto.isPaid()),
                () -> assertEquals(LocalDateTime.parse("2023-10-06T01:16:56"),checkDto.createdDate())
        );
    }

    @Test
    void getReceiptByOrderId() {
        Payment payment = new Payment(1L,1L,new BigDecimal(10), PaymentStatus.REJECTED, PaymentType.DEBIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        Receipt receipt = new Receipt(1L,1L,new BigDecimal(10),PaymentType.DEBIT_CARD,LocalDateTime.parse("2023-10-06T01:16:56"));

        Mockito.when(paymentRepository.findByOrderIdAndPaymentStatus(1L,PaymentStatus.PAID)).thenReturn(Optional.of(payment));
        Mockito.when(paymentMapper.toReceipt(payment)).thenReturn(receipt);

        Receipt receiptByOrderId = paymentService.getReceiptByOrderId(1L);

        assertAll(
                () -> assertEquals(1L,receiptByOrderId.id()),
                () -> assertEquals(1L,receiptByOrderId.orderId()),
                () -> assertEquals(PaymentType.DEBIT_CARD,receiptByOrderId.paymentType()),
                () -> assertEquals(LocalDateTime.parse("2023-10-06T01:16:56"),receiptByOrderId.createdDate())
        );
    }
}