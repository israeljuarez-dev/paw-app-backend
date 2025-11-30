package com.veterinary.paw.auth.controller;

import com.veterinary.paw.auth.dto.response.LoginResponseDTO;
import com.veterinary.paw.auth.dto.request.LoginRequestDTO;
import com.veterinary.paw.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request){
        LoginResponseDTO response = authService.login(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

}
