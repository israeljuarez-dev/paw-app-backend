package com.veterinary.paw.controller;

import com.veterinary.paw.dto.request.VeterinaryServiceCreateRequestDTO;
import com.veterinary.paw.dto.response.VeterinaryServiceResponseDTO;
import com.veterinary.paw.service.VeterinaryServiceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veterinary-services")
@RequiredArgsConstructor
@Validated
public class VeterinaryServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VeterinaryServiceController.class);

    private final VeterinaryServiceService veterinaryServiceService;

    @GetMapping("/{id}")
    public ResponseEntity<VeterinaryServiceResponseDTO> getServiceById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getServiceById() con ID: {}", id);
        VeterinaryServiceResponseDTO response = veterinaryServiceService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VeterinaryServiceResponseDTO>> getServices() {
        LOGGER.info("Ingresando al método getServices() para obtener todos los servicios");
        List<VeterinaryServiceResponseDTO> responses = veterinaryServiceService.get();

        if (responses.isEmpty()) {
            LOGGER.warn("No se encontraron servicios veterinarios registrados");
            return ResponseEntity.noContent().build();
        }

        LOGGER.info("Se encontraron {} servicios veterinarios registrados", responses.size());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<VeterinaryServiceResponseDTO> registerService(
            @RequestBody @Valid VeterinaryServiceCreateRequestDTO request
    ){
        LOGGER.info("➡Ingresando al método registerService() con datos: name={}, price={}",
                request.name(), request.price());

        VeterinaryServiceResponseDTO response = veterinaryServiceService.register(request);

        LOGGER.info("Registro exitoso. Nuevo servicio ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinaryServiceResponseDTO> updateService(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid VeterinaryServiceCreateRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updateService() con ID: {} y datos: name={}, price={}",
                id, request.name(), request.price());

        VeterinaryServiceResponseDTO response = veterinaryServiceService.update(id, request);

        LOGGER.info("Actualización exitosa. Servicio ID: {}", response.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método deleteService() con ID: {}", id);
        veterinaryServiceService.delete(id);

        LOGGER.info("Eliminación exitosa. Servicio ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
