package com.veterinary.paw.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record VeterinaryServiceCreateRequestDTO(
        String name,
        String description,
        BigDecimal price
) {
}
