package com.veterinary.paw.service;

import com.veterinary.paw.domain.UserEntity;
import com.veterinary.paw.dto.request.UserCreateRequestDTO;
import com.veterinary.paw.dto.response.UserResponseDTO;
import com.veterinary.paw.enums.ApiErrorEnum;
import com.veterinary.paw.exception.PawException;
import com.veterinary.paw.mapper.UserMapper;
import com.veterinary.paw.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEntityService {
    private static Logger LOGGER = LoggerFactory.getLogger(UserEntityService.class);

    private final UserEntityRepository userEntityRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> get() {
        return userEntityRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getById(Long id) {
        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Usuario no encontrado ID: {}", id);
                    return new PawException(ApiErrorEnum.USER_NOT_FOUND);
                });

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO register(UserCreateRequestDTO request) {
        if (userEntityRepository.existsByEmail(request.email())) {
            LOGGER.error("Usuario con el email: {} ya existe", request.email());
            throw new PawException(ApiErrorEnum.USER_EMAIL_ALREADY_EXISTS);
        }

        if (userEntityRepository.existsByDni(request.dni())){
            LOGGER.error("El DNI: {} ya pertenece a otro usuario.", request.dni());
            throw new PawException(ApiErrorEnum.USER_DNI_ALREADY_EXISTS);
        }

        if (userEntityRepository.existsByPhoneNumber(request.phoneNumber())){
            LOGGER.error("El número de teléfono: {} ya pertenece a otro usuario.", request.phoneNumber());
            throw new PawException(ApiErrorEnum.USER_PHONE_NUMBER_ALREADY_EXISTS);
        }

        UserEntity newUser = userMapper.toEntity(request);

        newUser.setPassword(passwordEncoder.encode(request.password()));

        UserEntity savedUser = userEntityRepository.save(newUser);
        LOGGER.info("Usuario registrado exitosamente con email: {}", savedUser.getEmail());
        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserCreateRequestDTO request) {
        UserEntity userToUpdate = userEntityRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Usuario no encontrado para actualizar ID: {}", id);
                    return new PawException(ApiErrorEnum.USER_NOT_FOUND);
                });

        if (!userToUpdate.getEmail().equals(request.email())) {
            if (userEntityRepository.existsByEmail(request.email())) {
                LOGGER.error("El email: {} ya pertenece a otro usuario.", request.email());
                throw new PawException(ApiErrorEnum.USER_EMAIL_ALREADY_EXISTS);
            }
        }

        if (!userToUpdate.getDni().equals(request.dni())) {
            if (userEntityRepository.existsByDni(request.dni())) {
                LOGGER.error("El DNI: {} ya pertenece a otro usuario.", request.dni());
                throw new PawException(ApiErrorEnum.USER_DNI_ALREADY_EXISTS);
            }
        }

        if (!userToUpdate.getPhoneNumber().equals(request.phoneNumber())) {
            if (userEntityRepository.existsByPhoneNumber(request.phoneNumber())) {
                LOGGER.error("El número de teléfono: {} ya pertenece a otro usuario.", request.phoneNumber());
                throw new PawException(ApiErrorEnum.USER_PHONE_NUMBER_ALREADY_EXISTS);
            }
        }

        userMapper.updateEntityFromDTO(userToUpdate, request);

        UserEntity updatedUser = userEntityRepository.save(userToUpdate);
        LOGGER.info("Usuario actualizado exitosamente ID: {}", id);
        return userMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        if (!userEntityRepository.existsById(id)) {
            LOGGER.error("Usuario no encontrado para eliminar ID: {}", id);
            throw new PawException(ApiErrorEnum.USER_NOT_FOUND);
        }

        userEntityRepository.deleteById(id);
        LOGGER.info("Usuario eliminado exitosamente ID: {}", id);
    }
}
