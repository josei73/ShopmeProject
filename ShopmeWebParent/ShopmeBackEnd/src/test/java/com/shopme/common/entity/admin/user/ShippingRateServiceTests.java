package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.admin.user.user.exception.ShippingRateNotFound;
import com.shopme.common.entity.admin.user.user.repository.ProductRepository;
import com.shopme.common.entity.admin.user.user.repository.ShippingRateRepository;
import com.shopme.common.entity.admin.user.user.service.ShippingRateService;
import com.shopme.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTests {


    @MockBean private ShippingRateRepository shippingRateRepository;
    @MockBean private ProductRepository productRepository;

    @InjectMocks
    private ShippingRateService shipService;


    @Test
    public void testCalculateShippingCost_NoRateFound(){
        Integer productId = 1;
        Integer countryId = 234;
        String state = "ABC";


        Mockito.when(shippingRateRepository.findByCountryIdAndState(countryId,state)).thenReturn(null);

        assertThrows(ShippingRateNotFound.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                shipService.calculateShippingCost(productId,countryId,state);
            }
        });
    }


    @Test
    public void testCalculateShippingCost_RateFound() throws ShippingRateNotFound {
        Integer productId = 1;
        Integer countryId = 234;
        String state = "New York";


        ShippingRate shippingRate = new ShippingRate();

        shippingRate.setRate(10);

        Mockito.when(shippingRateRepository.findByCountryIdAndState(countryId,state)).thenReturn(shippingRate);
        Product product = new Product();
        product.setWeight(5);
        product.setWidth(4);
        product.setHeight(3);
        product.setLength(8);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        float shippingCost = shipService.calculateShippingCost(productId, countryId, state);

        assertEquals(50,shippingCost);


    }
}
