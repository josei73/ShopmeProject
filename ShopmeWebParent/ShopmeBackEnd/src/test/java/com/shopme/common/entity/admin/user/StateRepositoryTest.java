package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

import com.shopme.common.entity.admin.user.user.repository.StateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {
    @Autowired
    private StateRepository repo;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void testCreateState() {
        Integer countryId = 3;
        Country country = entityManager.find(Country.class, countryId);
        State state = repo.save(new State("London", country));
        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    // wie bei den anderen


}
