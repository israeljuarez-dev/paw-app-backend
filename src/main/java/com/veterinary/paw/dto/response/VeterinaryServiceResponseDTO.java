package com.veterinary.paw.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record VeterinaryServiceResponseDTO(

        Long id,

        String name,

        String description,

        BigDecimal price
) {
}
