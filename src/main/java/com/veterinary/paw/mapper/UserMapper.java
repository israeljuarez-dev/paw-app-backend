package com.veterinary.paw.mapper;

import com.veterinary.paw.domain.UserEntity;
import com.veterinary.paw.dto.request.UserCreateRequestDTO;
import com.veterinary.paw.dto.response.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserCreateRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        String encodedPassword = dto.password();

        return UserEntity.builder()
                .email(dto.email())
                .password(encodedPassword)
                .dni(dto.dni())
                .email(dto.email())
                .phoneNumber(dto.phoneNumber())
                .build();
    }

    public UserResponseDTO toResponseDTO(UserEntity user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .message("Usuario registrado correctamente")
                .status(HttpStatus.CREATED.value())
                .build();
    }

    public void updateEntityFromDTO(UserEntity user, UserCreateRequestDTO dto) {
        if (user != null && dto != null) {
            user.setEmail(dto.email());
            user.setDni(dto.dni());
            user.setPhoneNumber(dto.phoneNumber());

            String encodedPassword = dto.password();
            user.setPassword(encodedPassword);
        }
    }
}
