package com.veterinary.paw.auth.service;

import com.veterinary.paw.auth.dto.response.LoginResponseDTO;
import com.veterinary.paw.auth.jwt.service.JwtService;
import com.veterinary.paw.domain.UserEntity;
import com.veterinary.paw.enums.ApiErrorEnum;
import com.veterinary.paw.exception.PawException;
import com.veterinary.paw.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserEntityRepository userEntityRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public UserEntity findUserByEmail(String email){
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("Usuario no encontrado con el email: {}", email);
                    return new PawException(ApiErrorEnum.INVALID_CREDENTIALS); // O USER_NOT_FOUND si no quieres ser tan genérico
                });
    }

    public LoginResponseDTO login(String email, String password){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (RuntimeException exception) {
            LOGGER.error("Error de autenticación para el email: {}", email, exception);
            throw new PawException(ApiErrorEnum.INVALID_CREDENTIALS);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);
        return LoginResponseDTO.builder()
                .email(userDetails.getUsername())
                .message("Autenticación exitosa")
                .token(jwt)
                .build();
    }
}
