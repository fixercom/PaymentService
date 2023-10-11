package ru.aston.intensive.paymentservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.aston.intensive.paymentservice.dao.entity.Payment;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;

import java.util.UUID;

@Mapper
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "paymentType", ignore = true)
    @Mapping(target = "checkAmount", source = "newPaymentDto.totalPrice")
    Payment toPayment(UUID orderId, NewPaymentDto newPaymentDto);

    @Mapping(target = "isPaid", expression = "java(covertPaymentStatusToBooleanType(payment))")
    PaymentDto toPaymentDto(Payment payment);

    Receipt toReceipt(Payment payment);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Payment updatePayment(Payment source, @MappingTarget Payment target);

    default boolean covertPaymentStatusToBooleanType(Payment payment) {
        return payment.getPaymentStatus() == PaymentStatus.PAID;
    }

}
