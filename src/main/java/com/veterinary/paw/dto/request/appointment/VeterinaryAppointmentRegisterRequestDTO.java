package com.veterinary.paw.dto.request.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record VeterinaryAppointmentRegisterRequestDTO(

        @JsonProperty("customer_information")
        @Valid
        CustomerInfoDTO customerInfo,

        @JsonProperty("pet_information")
        @Valid
        PetInfoDTO petInfo,

        String observations,

        @JsonProperty("veterinary_id")
        Long veterinaryId,

        @JsonProperty("veterinary_service_id")
        Long veterinaryServiceId,

        @JsonProperty("shift_id")
        Long shiftId
) {
}
