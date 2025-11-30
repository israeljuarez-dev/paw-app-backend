package com.veterinary.paw.auth.service;

import com.veterinary.paw.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserEntityRepository userEntityRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("No se encontró el usuario con email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });
    }
}
