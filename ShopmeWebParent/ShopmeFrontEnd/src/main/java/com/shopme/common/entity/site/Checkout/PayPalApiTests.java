package com.shopme.common.entity.site.Checkout;


import org.junit.jupiter.api.Test;


import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class PayPalApiTests {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AWTP3U5sI-gGQJ0T8m06HWMEss-GBKP5cFzH55UH_R9n1dU6av2bjU2VV4ngGX3_GOvIgnsq9IEZELOV";
    private static final String CLIENT_SECRETE = "EMdqpjStQK1dcWXyK2EUQLv6YHCVdEjWCOCBOMQvcXl8hGkFvwI59vl_SVUkGi-k785KfKQXNhrGkkL-";


    @Test
    public void testGetOrderDetails() {
        String orderId = "1C039973JX309644M";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_Us");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRETE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(requestURL);
        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);

        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println("Order ID: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));

    }
}
