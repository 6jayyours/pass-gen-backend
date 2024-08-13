package com.app.password_generator.service;

import com.app.password_generator.entity.User;
import com.app.password_generator.entity.Passwords;
import com.app.password_generator.repository.PasswordsRepository;
import com.app.password_generator.repository.UserRepository;
import com.app.password_generator.requestresponse.LoginRequest;
import com.app.password_generator.requestresponse.LoginResponse;
import com.app.password_generator.requestresponse.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordsRepository passwordsRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, PasswordsRepository passwordsRepository) {
        this.userRepository = userRepository;
        this.passwordsRepository = passwordsRepository;
    }

    public String registerUser(RegisterRequest request) {
        try {
            // Check if the user already exists
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return "Username already exists";
            }

            // Create a new user and encode the password
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // Save the user to the repository
            userRepository.save(user);

            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Registration failed due to an unexpected error";
        }
    }

    public LoginResponse loginUser(LoginRequest request) {
        try {
            Optional<User> OptionalUser = userRepository.findByUsername(request.getUsername());

            if (OptionalUser.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            User user = OptionalUser.get();

            // Check if the password matches
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Create the response with username and passwords
                Set<Passwords> passwordEntities = passwordsRepository.findByUserId(user.getId());
                Set<String> passwords = passwordEntities.stream()
                        .map(Passwords::getPassword) // Extract password field
                        .collect(Collectors.toSet());


                return new LoginResponse(user.getUsername(), passwords,"login successfull");
            } else {
                return new LoginResponse(null,null,"Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Login failed due to an unexpected error");
        }
    }

    public LoginResponse savePassword(String username, String rawPassword) {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isEmpty()) {
                return new LoginResponse(username, new HashSet<>(), "User not found");
            }

            User user = optionalUser.get();

            // Create a new password entity
            Passwords passwordEntity = new Passwords();
            passwordEntity.setPassword(rawPassword);
            passwordEntity.setUser(user);

            // Save the new password
            passwordsRepository.save(passwordEntity);

            Set<Passwords> passwordEntities = passwordsRepository.findByUserId(user.getId());
            Set<String> passwords = passwordEntities.stream()
                    .map(Passwords::getPassword) // Extract password field
                    .collect(Collectors.toSet());

            return new LoginResponse(username, passwords, "Password saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(username, new HashSet<>(), "Failed to save password due to an unexpected error");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
