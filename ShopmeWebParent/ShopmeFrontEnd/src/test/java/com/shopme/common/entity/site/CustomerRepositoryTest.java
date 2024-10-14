package com.shopme.common.entity.site;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.repository.CustomerRepository;
import com.shopme.common.enums.AuthenticationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;
    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void createCustomer() {
        Integer countryId = 106;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Jahangir");
        customer.setLastName("fufu");
        customer.setPassword("password123");
        customer.setEmail("jahangir.33@gmail.com");
        customer.setPhoneNumber("01239303");
        customer.setCity("Mumbai");
        customer.setState("Mahrashat");
        customer.setPostalCode("95867");
        customer.setAddressLine1("1921 West Drive");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers() {
        List<Customer> customerList = repository.findAll();
        customerList.forEach(System.out::println);
        assertThat(customerList.size()).isGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer() {
        Integer customerId = 1;
        String lastName = "Hower";
        Customer customer = repository.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(false);
        customer.setVerificationCode("code_1234");
        Customer updatedCustomer = repository.save(customer);
        assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);

    }

    @Test
    public void testFindByEmail() {
        String email = "jahangir.33@gmail.com";
        Customer customer = repository.findByEmail(email);
        if(customer == null)
            System.out.println("Null");
        assertThat(customer).isNull();
        System.out.println(customer);

    }

    @Test
    public void testFindByVerificationCode() {
        String code = "code_123";
        Customer customer = repository.findByVerificationCode(code);
        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void enabledCustomer() {
        Integer customerId = 1;
        repository.enabled(customerId);
        Customer customer = repository.findById(customerId).get();
        assertThat(customer.isEnabled()).isTrue();

    }

    @Test
    public void deleteCustomer(){
        Integer customerId = 2;
        repository.deleteById(customerId);
    }

    @Test
    public void testUpdateAuthenticationType(){
       Integer id = 1;
       repository.updateAuthenticationType(1, AuthenticationType.DATABASE);

       Customer customer = repository.findById(id).get();

       assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);

    }
}
