package ru.aston.intensive.paymentservice.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.intensive.paymentservice.dao.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

}
