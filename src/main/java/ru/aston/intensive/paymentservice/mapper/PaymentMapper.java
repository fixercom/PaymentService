package ru.aston.intensive.paymentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;

@Mapper
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    Payment toPayment(Long orderId, NewPaymentDto newPaymentDto);

    PaymentDto toPaymentDto(Payment payment);

    Receipt toReceipt(Payment payment);

}
