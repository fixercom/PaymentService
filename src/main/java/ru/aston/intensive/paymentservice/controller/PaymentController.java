package ru.aston.intensive.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;
import ru.aston.intensive.paymentservice.service.PaymentService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payment/{orderId}/check")
    public Receipt getReceiptByOrderId(@PathVariable UUID orderId) {
        return paymentService.getReceiptByOrderId(orderId);
    }

    @PostMapping("/pay/{orderId}")
    public PaymentDto payOrder(@PathVariable UUID orderId, @RequestBody NewPaymentDto newPaymentDto) {
        return paymentService.payOrder(orderId, newPaymentDto);
    }

}
