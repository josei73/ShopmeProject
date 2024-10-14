package com.shopme.common.entity.site;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void addNew() {
        Integer countryId = 58;
        Integer customerId = 10;
        Country country = entityManager.find(Country.class, countryId);
        Customer customer = entityManager.find(Customer.class, customerId);
        Address address = new Address();
        address.setCountry(country);
        address.setFirstName("Thomas");
        address.setLastName("Meyer");
        address.setPhoneNumber("01239303");
        address.setCity("Hamburg");
        address.setState("Hamburg");
        address.setPostalCode("03044");
        address.setAddressLine1("Alster 38");
        address.setCustomer(customer);
        address.setDefaultForShipping(true);

        Address savedAddress = repository.save(address);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }


    @Test
    public void findBYCustomer() {
        Integer customerId = 10;
        Customer customer = entityManager.find(Customer.class, customerId);
        List<Address> addresses = repository.findByCustomer(customer);
        addresses.forEach(System.out::println);

        assertThat(addresses.size()).isEqualTo(2);
    }

    @Test
    public void testFindBYIdAnCustomer() {
        Integer customerId = 12;
        Integer addressId = 2;
        Address address = repository.findByIdAndCustomerId(addressId, customerId);

        assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testDeleteByIdAndCustomer() {
        Integer customerId = 10;
        Integer addressId = 3;
        repository.deleteByIdAndCustomerId(addressId, customerId);
        Optional<Address> add = repository.findById(addressId);
        assertThat(add).isNotPresent();
    }

    @Test
    public void updateAddress() {
        Integer addressId = 13;
        Address address = repository.findById(addressId).get();
        address.setDefaultForShipping(true);
        repository.save(address);
    }


    @Test
    public void testSetDefault() {
        Integer addressId = 4;
        repository.setDefaultAddress(addressId);
        Address address = repository.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();
    }


    @Test
    public void testNonDefault() {
        Integer addressId = 4;
        Integer customerId = 10;
        repository.setNonDefaultForOthers(addressId,customerId);

    }

    @Test
    public void testGetDefault(){
        Integer customerId = 5;
        Address defaultCustomer = repository.findDefaultCustomer(customerId);
        assertThat(defaultCustomer).isNotNull();
        System.out.println(defaultCustomer);
    }




}
