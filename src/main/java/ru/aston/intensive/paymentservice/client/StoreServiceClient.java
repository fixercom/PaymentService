package ru.aston.intensive.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.aston.intensive.paymentservice.dto.Receipt;

@FeignClient(value = "storeServiceClient", url = "http://localhost:8088")
public interface StoreServiceClient {

    @PostMapping(value = "/stores/payments")
    void sendReceiptToStore(@RequestBody Receipt receipt);

}
