package com.shopme.common.entity.site;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.site.repository.ShippingRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShippingRateRepositoryTest {

    @Autowired
    private ShippingRateRepository rateRepository;

    @Test
    public void findByCountryNState() {
        Country country = new Country(234);
        String state = "New York";
        ShippingRate shippingRate = rateRepository.findByCountryAndState(country, state);
        assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }
}
