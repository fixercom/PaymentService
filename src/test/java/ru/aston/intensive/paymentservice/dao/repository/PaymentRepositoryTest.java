package ru.aston.intensive.paymentservice.dao.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.aston.intensive.paymentservice.AbstractIntegrationTest;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PaymentRepositoryTest extends AbstractIntegrationTest {

    private final TestEntityManager entityManager;
    private final PaymentRepository paymentRepository;

    @Test
    void findByOrderIdTest() {
        LocalDateTime dateTime = LocalDateTime.of(2024,10,7,16,0);
        Payment payment = Payment.builder()
                .orderId(400L)
                .checkAmount(new BigDecimal(63900))
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.DEBIT_CARD)
                .createdDate(dateTime)
                .build();
        entityManager.persist(payment);

        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(400L);
        Payment loadedPayment = optionalPayment.orElse(new Payment());
        assertAll(
                () -> assertTrue(optionalPayment.isPresent()),
                () -> assertNotNull(loadedPayment.getId()),
                () -> assertEquals(400L, loadedPayment.getOrderId()),
                () -> assertEquals(BigDecimal.valueOf(63900), loadedPayment.getCheckAmount()),
                () -> assertEquals(PaymentStatus.PAID, loadedPayment.getPaymentStatus()),
                () -> assertEquals(PaymentType.DEBIT_CARD, loadedPayment.getPaymentType()),
                () -> assertEquals(dateTime, loadedPayment.getCreatedDate())
        );

    }

    @Test
    void findByOrderIdAndPaymentStatus() {
        Payment payment1 = Payment.builder()
                .orderId(400L)
                .checkAmount(new BigDecimal(63900))
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.DEBIT_CARD)
                .createdDate(LocalDateTime.now())
                .build();
        entityManager.persist(payment1);

        assertAll(
                () -> assertTrue(paymentRepository
                        .findByOrderIdAndPaymentStatus(400L, PaymentStatus.PAID).isPresent()),
                () -> assertFalse(paymentRepository
                        .findByOrderIdAndPaymentStatus(400L, PaymentStatus.REJECTED).isPresent())
        );
    }

}
