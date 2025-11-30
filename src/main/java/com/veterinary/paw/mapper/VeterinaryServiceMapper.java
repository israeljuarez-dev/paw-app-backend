package com.veterinary.paw.mapper;

import com.veterinary.paw.domain.VeterinaryService;
import com.veterinary.paw.dto.request.VeterinaryServiceCreateRequestDTO;
import com.veterinary.paw.dto.response.VeterinaryServiceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VeterinaryServiceMapper {

    public VeterinaryServiceResponseDTO toResponseDTO(VeterinaryService veterinaryService) {
        if (veterinaryService == null) return null;

        return VeterinaryServiceResponseDTO.builder()
                .id(veterinaryService.getId())
                .name(veterinaryService.getName())
                .description(veterinaryService.getDescription())
                .price(veterinaryService.getPrice())
                .build();
    }


    public VeterinaryService toEntity(VeterinaryServiceCreateRequestDTO request) {
        if (request == null) return null;

        return VeterinaryService.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();
    }

    public void updateEntityFromDTO(VeterinaryService entity, VeterinaryServiceCreateRequestDTO request) {
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
    }
}
