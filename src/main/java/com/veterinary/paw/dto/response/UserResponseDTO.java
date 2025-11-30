package com.veterinary.paw.dto.response;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        Long id,
        String email
) {
}
