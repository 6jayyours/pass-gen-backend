package com.app.password_generator.requestresponse;

import lombok.Data;

@Data
public class SavePasswordRequest {
    private String username;
    private String password;
}