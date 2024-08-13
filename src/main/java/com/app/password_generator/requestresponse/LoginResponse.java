package com.app.password_generator.requestresponse;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String username;
    private Set<String> passwords; // Adjust the type to match your password format
    private String message;
}