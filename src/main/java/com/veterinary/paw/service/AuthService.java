package com.veterinary.paw.service;

import com.veterinary.paw.domain.UserEntity;
import com.veterinary.paw.enums.ApiErrorEnum;
import com.veterinary.paw.exception.PawException;
import com.veterinary.paw.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserEntityRepository userEntityRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity authenticate(String email, String password){
        UserEntity user = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("Usuario no encontrado con el email: {}", email);
                    return new PawException(ApiErrorEnum.INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(password, user.getPassword())){
            LOGGER.error("Contraseña incorrecta");
            throw new PawException(ApiErrorEnum.INVALID_CREDENTIALS);
        }

        return user;
    }
}
