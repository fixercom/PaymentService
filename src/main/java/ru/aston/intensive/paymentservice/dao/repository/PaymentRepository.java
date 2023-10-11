package ru.aston.intensive.paymentservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(UUID orderId);

    Optional<Payment> findByOrderIdAndPaymentStatus(UUID orderId, PaymentStatus paymentStatus);

}
