package ru.aston.intensive.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.aston.intensive.paymentservice.dao.enums.PaymentStatus;
import ru.aston.intensive.paymentservice.dao.enums.PaymentType;
import ru.aston.intensive.paymentservice.dto.NewPaymentDto;
import ru.aston.intensive.paymentservice.dto.PaymentDto;
import ru.aston.intensive.paymentservice.dto.Receipt;
import ru.aston.intensive.paymentservice.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;


    @Test
    void getReceiptByOrderId() throws Exception {
        Receipt receipt = new Receipt(1L, 10L, new BigDecimal(1), PaymentType.CREDIT_CARD, LocalDateTime.parse("2023-10-06T01:16:56"));
        when(paymentService.getReceiptByOrderId(1L)).thenReturn(receipt);
        mockMvc.perform(MockMvcRequestBuilders.get("/payment/1/check"))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(receipt.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("orderId").value(receipt.orderId()))
                .andExpect(MockMvcResultMatchers.jsonPath("checkAmount").value(receipt.checkAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("paymentType").value(receipt.paymentType().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("createdDate").value(receipt.createdDate().toString()));
    }

    @Test
    void payOrder() throws Exception {
        NewPaymentDto newPaymentDto = new NewPaymentDto(1L,new BigDecimal(10),PaymentType.DEBIT_CARD);
        PaymentDto paymentDto = new PaymentDto(1L,10L, PaymentStatus.PAID,LocalDateTime.parse("2023-10-06T01:16:56"));
        when(paymentService.payOrder(1L,newPaymentDto)).thenReturn(paymentDto);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/pay/1")
                .content(asJsonString(newPaymentDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(paymentDto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("orderId").value(paymentDto.orderId()))
                .andExpect(MockMvcResultMatchers.jsonPath("paymentStatus").value(paymentDto.paymentStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("createdDate").value(paymentDto.createdDate().toString()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}