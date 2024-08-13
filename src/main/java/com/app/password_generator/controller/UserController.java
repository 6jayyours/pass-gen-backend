package com.app.password_generator.controller;

import com.app.password_generator.requestresponse.LoginRequest;
import com.app.password_generator.requestresponse.LoginResponse;
import com.app.password_generator.requestresponse.RegisterRequest;
import com.app.password_generator.requestresponse.SavePasswordRequest;
import com.app.password_generator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        String result = userService.registerUser(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            LoginResponse result = userService.loginUser(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(new LoginResponse(null, null,"login failure")); // Or any other appropriate error handling
        }
    }

    @PostMapping("/savepass")
    public ResponseEntity<LoginResponse> savePassword(@RequestBody SavePasswordRequest request) {
        LoginResponse response = userService.savePassword(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
