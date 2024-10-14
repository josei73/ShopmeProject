package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Currency;
import com.shopme.common.entity.admin.user.user.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTest {

    @Autowired private CurrencyRepository repository;

    @Test
    public void testCreateCurrencies(){
        List<Currency> currencies = Arrays.asList(
                new Currency("British Pound","£","GBP"),
                new Currency("Japanese Yen","¥","JPY"),
                new Currency("Russia Ruble","₽","RUB"),
                new Currency("South Korean Won","₩","KRW"),
                new Currency("China Yuan","¥","CNY"),
                new Currency("Brazil Real","R$","BRL"),
                new Currency("Australia Dollar","$","AUD"),
                new Currency("Canadian Dollar","$","CAD"),
                new Currency("Vietnamese dong","₫","VND"),
                new Currency("India Rupee","₹","INR")

        );
        repository.saveAll(currencies);
        Iterable<Currency> iterable = repository.findAll();
        assertThat(iterable).size().isEqualTo(12);
    }

    @Test
    public void testListAllOrderByNameAsc(){
        var currencies = repository.findAllByOrderByNameAsc();
        currencies.forEach(System.out::println);
        assertThat(currencies.size()).isGreaterThan(0);
    }
}
