package ru.aston.intensive.paymentservice.mapper;

import org.mapstruct.Mapper;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;

@Mapper
public interface PaymentMapper {

    Payment toPayment(NewPaymentDto newPaymentDto);

    PaymentDto toPaymentDto(Payment payment);

    Receipt toReceipt(Payment payment);
}
