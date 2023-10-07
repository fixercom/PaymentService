package ru.aston.intensive.paymentservice.dao.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PaymentRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");
    private final TestEntityManager entityManager;
    private final PaymentRepository paymentRepository;

    @Test
    void findByOrderIdTest() {
        Payment payment = Payment.builder()
                .orderId(400L)
                .checkAmount(new BigDecimal(63900))
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.DEBIT_CARD)
                .createdDate(LocalDateTime.now())
                .build();
        entityManager.persist(payment);

        assertTrue(paymentRepository.findByOrderId(400L).isPresent());
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
