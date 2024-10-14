package com.shopme.common.entity.admin.user;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.admin.user.user.repository.ShippingRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.Table;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShippingRateRepositoryTest {

    @Autowired
    private ShippingRateRepository repository;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void testCreateNew() {
        Integer countryId = 14;
        Country country = entityManager.find(Country.class, countryId);
        ShippingRate shippingRate = new ShippingRate();
        shippingRate.setCountry(country);
        shippingRate.setState("Qeens");
        shippingRate.setDays(4);
        shippingRate.setRate(15.5F);
        shippingRate.setCodSupported(true);
        ShippingRate saveRate = repository.save(shippingRate);

        assertThat(saveRate).isNotNull();
        assertThat(saveRate.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdate() {
        Integer id = 2;
        String state = "Freiburg";
        ShippingRate shippingRate = repository.findById(id).get();
        shippingRate.setState(state);
        ShippingRate saveRate = repository.save(shippingRate);

        assertThat(saveRate.getState()).isEqualTo(state);
    }

    @Test
    public void findAll() {
        List<ShippingRate> rates = repository.findAll();
        rates.forEach(System.out::println);

        assertThat(rates.size()).isEqualTo(3);
    }

    @Test
    public void findByCountryNState() {
        Integer countryId = 58;
        String state = "Bremen";
        ShippingRate shippingRate = repository.findByCountryIdAndState(countryId, state);

        assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }

    @Test
    public void updateCodSupport(){
        Integer id = 2;
        boolean enabled = false;
        repository.updateCodSupport(id,enabled);
        ShippingRate shippingRate = repository.findById(2).get();

        assertThat(!shippingRate.isCodSupported()).isTrue();
    }


}
