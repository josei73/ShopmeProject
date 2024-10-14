package com.shopme.common.entity.admin.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.model.StateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;


    @Test
    @WithMockUser(username = "kwame@codejava.net", password = "something", roles = "ADMIN")
    public void testListByCountries() throws Exception {

        Country country = entityManager.find(Country.class, 2);
        String url = "/states/list_by_country/" + country.getId();
        MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        StateDTO[] states = objectMapper.readValue(jsonResponse, StateDTO[].class);
        assertThat(states).hasSizeGreaterThan(0);
    }


    @Test
    @WithMockUser(username = "kwame@codejava.net", password = "something", roles = "ADMIN")
    public void testCreateState() throws Exception {
        String url = "/states/save/";
        Country country = entityManager.find(Country.class, 2);

        String stateName = "Berlin";
        State state = new State(stateName, country);
        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        State savedState = entityManager.find(State.class, stateId);
        assertThat(savedState).isNotNull();
        assertThat(savedState.getName()).isEqualTo(stateName);
    }

    @Test
    @WithMockUser(username = "kwame@codejava.net", password = "something", roles = "ADMIN")
    public void testUpdateState() throws Exception {
        String url = "/states/save/";
        State state = entityManager.find(State.class, 13);
        Country country = entityManager.find(Country.class, 9);

        String stateName = "Kumasi";
        state.setName(stateName);
        state.setCountry(country);
        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(response);
        State savedState = entityManager.find(State.class, stateId);
        assertThat(savedState).isNotNull();
        assertThat(savedState.getName()).isEqualTo(stateName);
    }

    @Test
    @WithMockUser(username = "kwame@codejava.net", password = "something", roles = "ADMIN")
    public void testDeleteState() throws Exception {
        Integer stateId = 14;
        String url = "/states/delete/"+stateId;
        mockMvc.perform(get(url)).andExpect(status().isOk());

        State state = entityManager.find(State.class,stateId);
        assertThat(state).isNull();
    }


}
