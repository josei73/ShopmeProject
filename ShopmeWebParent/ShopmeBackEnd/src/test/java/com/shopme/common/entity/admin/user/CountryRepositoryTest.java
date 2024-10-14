package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.admin.user.user.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTest {
    @Autowired
    private CountryRepository repo;


    @Test
    public void testCreateCountry() {
        Country country = repo.save(new Country("China", "CH"));
        assertThat(country).isNotNull();
        assertThat(country.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCountries() {
        List<Country> countryList = repo.findAll();
        countryList.forEach(System.out::println);
        assertThat(countryList.size()).isGreaterThan(0);
    }

    @Test
    public void updateCountry() {
        Integer id = 4;
        String code = "USA";
        Country country = repo.findById(id).get();
        country.setCode(code);
        Country savedCountry = repo.save(country);
        assertThat(savedCountry.getCode()).isEqualTo(code);
    }

    @Test
    public void getCountry(){
        Integer id = 2;
        Country country = repo.findById(id).get();
        assertThat(country).isNotNull();
    }


    @Test
    public void deleteCountry(){
        Integer id = 5;
        repo.deleteById(id);
        Optional<Country> country = repo.findById(id);
        assertThat(country.isEmpty());

    }
}
