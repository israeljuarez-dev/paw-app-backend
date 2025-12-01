package com.veterinary.paw.dto.response;

import lombok.Builder;

@Builder
public record ResponseDTO(
        String message,
        int status,
        Object data
) {
}
