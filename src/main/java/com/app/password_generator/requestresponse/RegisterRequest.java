package com.app.password_generator.requestresponse;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
