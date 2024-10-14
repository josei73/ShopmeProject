package com.shopme.common.entity.admin.user.user.model;

import com.shopme.common.entity.State;
import com.shopme.common.model.StateDTO;
import org.springframework.stereotype.Service;


import java.util.function.Function;

@Service
public class StateDTOMapper implements Function<State, StateDTO> {
    @Override
    public StateDTO apply(State state) {
        return new StateDTO(state.getId(), state.getName());
    }
}
