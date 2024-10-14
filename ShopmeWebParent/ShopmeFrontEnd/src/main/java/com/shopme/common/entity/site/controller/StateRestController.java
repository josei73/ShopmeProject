package com.shopme.common.entity.site.controller;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.site.repository.StateRepository;
import com.shopme.common.model.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;



    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
        List<StateDTO> stateDTOS = new ArrayList<>();


            List<State> states = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
            states.forEach(state -> {
                stateDTOS.add(new StateDTO(state.getId(), state.getName()));
            });

        return stateDTOS;
    }


}
