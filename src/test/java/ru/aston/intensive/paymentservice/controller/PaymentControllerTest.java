package ru.aston.intensive.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;
import ru.aston.intensive.paymentservice.exception.OrderIsAlreadyPaidException;
import ru.aston.intensive.paymentservice.exception.OrderIsNotPaidException;
import ru.aston.intensive.paymentservice.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;


    @Test
    void getReceiptByOrderId_whenCorrectRequest_thenCorrectResponse() throws Exception {
        UUID uuid = UUID.randomUUID();
        Receipt receipt = new Receipt(1L, uuid, new BigDecimal(1), PaymentType.CREDIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        when(paymentService.getReceiptByOrderId(uuid)).thenReturn(receipt);
        String urlTemplate = String.format("/payment/%s/check", uuid);
        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(receipt.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("orderId").value(receipt.orderId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("checkAmount").value(receipt.checkAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("paymentType").value(receipt.paymentType().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("createdDate").value(receipt.createdDate().toString()));
    }

    @Test
    void getReceiptByOrderId_whenOrderIsNotPaid_thenNotFoundResponse() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(paymentService.getReceiptByOrderId(uuid)).thenThrow(new OrderIsNotPaidException(uuid));
        String urlTemplate = String.format("/payment/%s/check", uuid);
        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                .andExpect(status().isNotFound());
    }

    @Test
    void payOrder_whenCorrectRequest_thenCorrectResponse() throws Exception {
        UUID uuid = UUID.randomUUID();
        NewPaymentDto newPaymentDto = new NewPaymentDto(new BigDecimal(10));
        PaymentDto paymentDto = new PaymentDto(uuid, true, LocalDateTime.parse("2023-10-06T01:16:56"));
        when(paymentService.payOrder(uuid, newPaymentDto)).thenReturn(paymentDto);
        String urlTemplate = String.format("/pay/%s", uuid);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlTemplate)
                        .content(asJsonString(newPaymentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("orderId").value(paymentDto.orderId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("isPaid").value(paymentDto.isPaid().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("createdDate").value(paymentDto.createdDate().toString()));
    }

    @Test
    void payOrder_whenOrderHasAlreadyPaid_thenConflictResponse() throws Exception {
        UUID uuid = UUID.randomUUID();
        NewPaymentDto newPaymentDto = new NewPaymentDto(new BigDecimal(10));
        when(paymentService.payOrder(uuid, newPaymentDto)).thenThrow(new OrderIsAlreadyPaidException(uuid));
        String urlTemplate = String.format("/pay/%s", uuid);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlTemplate)
                        .content(asJsonString(newPaymentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}