package ru.aston.intensive.paymentservice.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "check_amount", nullable = false)
    private BigDecimal checkAmount;

    @Column(name = "payment_status", length = 10)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_type", length = 10)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}