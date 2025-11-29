package com.veterinary.paw.service;

import com.veterinary.paw.dto.VeterinaryServiceCreateRequestDTO;
import com.veterinary.paw.dto.VeterinaryServiceResponseDTO;
import com.veterinary.paw.enums.ApiErrorEnum;
import com.veterinary.paw.exception.PawException;
import com.veterinary.paw.mapper.VeterinaryServiceMapper;
import com.veterinary.paw.repository.VeterinaryServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VeterinaryServiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VeterinaryServiceService.class);

    private final VeterinaryServiceRepository veterinaryServiceRepository;

    private final VeterinaryServiceMapper veterinaryServiceMapper;

    public List<VeterinaryServiceResponseDTO> get() {
        return veterinaryServiceRepository.findAll().stream()
                .map(veterinaryServiceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VeterinaryServiceResponseDTO getById(Long id) {
        com.veterinary.paw.domain.VeterinaryService veterinaryService = veterinaryServiceRepository.findById(id)
                .orElseThrow( () -> {
                    LOGGER.error("Servicio Veterinario no encontrado ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_SERVICE_NOT_FOUND);
                });

        return veterinaryServiceMapper.toResponseDTO(veterinaryService);
    }

    public VeterinaryServiceResponseDTO register(VeterinaryServiceCreateRequestDTO request) {
        if (veterinaryServiceRepository.existsByName(request.name())){
            LOGGER.error("El nombre de servicio: {} ya existe.", request.name());
            throw new PawException(ApiErrorEnum.VETERINARY_SERVICE_NAME_ALREADY_EXISTS);
        }

        com.veterinary.paw.domain.VeterinaryService newService = veterinaryServiceMapper.toEntity(request);

        com.veterinary.paw.domain.VeterinaryService savedService = veterinaryServiceRepository.save(newService);

        return veterinaryServiceMapper.toResponseDTO(savedService);
    }

    public VeterinaryServiceResponseDTO update(Long id, VeterinaryServiceCreateRequestDTO request) {
        com.veterinary.paw.domain.VeterinaryService serviceToUpdate = veterinaryServiceRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Servicio Veterinario no encontrado para actualizar ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_SERVICE_NOT_FOUND);
                });

        if (!serviceToUpdate.getName().equals(request.name())) {
            if (veterinaryServiceRepository.existsByName(request.name())) {
                LOGGER.error("El nombre de servicio: {} ya pertenece a otro servicio.", request.name());
                throw new PawException(ApiErrorEnum.VETERINARY_SERVICE_NAME_ALREADY_EXISTS);
            }
        }

        veterinaryServiceMapper.updateEntityFromDTO(serviceToUpdate, request);

        com.veterinary.paw.domain.VeterinaryService updatedService = veterinaryServiceRepository.save(serviceToUpdate);

        return veterinaryServiceMapper.toResponseDTO(updatedService);
    }

    public void delete(Long id) {
        if (!veterinaryServiceRepository.existsById(id)) {
            throw new PawException(ApiErrorEnum.VETERINARY_SERVICE_NOT_FOUND);
        }

        veterinaryServiceRepository.deleteById(id);
    }
}
