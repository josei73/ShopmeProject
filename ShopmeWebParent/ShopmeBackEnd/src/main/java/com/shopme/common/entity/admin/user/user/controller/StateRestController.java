package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.admin.user.user.repository.StateRepository;
import com.shopme.common.model.StateDTO;
import com.shopme.common.entity.admin.user.user.model.StateDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateDTOMapper stateDTOMapper;


    @GetMapping("/states/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {


        List<State> states = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        return states.stream().map(stateDTOMapper).collect(Collectors.toList());


    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state) {
        State savedState = stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("states/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        stateRepository.deleteById(id);
    }
}
