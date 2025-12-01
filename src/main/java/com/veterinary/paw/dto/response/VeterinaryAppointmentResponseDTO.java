package com.veterinary.paw.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veterinary.paw.enums.AppointmentStatusEnum;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VeterinaryAppointmentResponseDTO(

        Long id,

        AppointmentStatusEnum status,

        String observations,

        @JsonProperty("register_date")
        LocalDate registerDate,

        @JsonProperty("pet")
        PetResponseDTO pet,

        @JsonProperty("veterinary")
        VeterinaryResponseDTO veterinary,

        @JsonProperty("veterinary_service")
        VeterinaryServiceResponseDTO veterinaryService,

        @JsonProperty("shift")
        ShiftResponseDTO shift
) {
}
