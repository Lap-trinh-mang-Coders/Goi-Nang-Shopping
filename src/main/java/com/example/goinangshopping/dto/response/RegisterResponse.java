package com.example.goinangshopping.dto.response;

import com.example.goinangshopping.model.ActionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterResponse {
    private ActionStatus actionStatus;
    private String message;
    private String username;
}

