package com.veterinary.paw.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        String email,
        String message,
        String token,
        int status
) {
}
