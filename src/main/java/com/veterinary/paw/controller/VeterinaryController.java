package com.veterinary.paw.controller;

import com.veterinary.paw.dto.request.VeterinaryCreateRequestDTO;
import com.veterinary.paw.dto.response.ResponseDTO;
import com.veterinary.paw.dto.response.VeterinaryResponseDTO;
import com.veterinary.paw.service.VeterinaryService;
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
@RequestMapping("/veterinaries")
@RequiredArgsConstructor
@Validated
public class VeterinaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VeterinaryController.class);

    private final VeterinaryService veterinaryService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getVeterinaryById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getVeterinaryById() con ID: {}", id);
        VeterinaryResponseDTO veterinary = veterinaryService.getById(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Veterinario obtenido exitosamente!")
                .status(HttpStatus.OK.value())
                .data(veterinary)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getVeterinaries() {
        LOGGER.info("Ingresando al método getVeterinaries() para obtener todos los veterinarios");
        List<VeterinaryResponseDTO> veterinaries = veterinaryService.get();
        if (veterinaries.isEmpty()) {
            LOGGER.warn("No se encontraron veterinarios registrados");
            return ResponseEntity.noContent().build();
        }

        ResponseDTO response = ResponseDTO.builder()
                .message("Veterinarios obtenidos exitosamente!")
                .status(HttpStatus.OK.value())
                .data(veterinaries)
                .build();

        LOGGER.info("Se encontraron {} veterinarios registrados", veterinaries.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> registerVeterinary(
            @RequestBody @Valid VeterinaryCreateRequestDTO request
    ){
        LOGGER.info("➡Ingresando al método registerVeterinary() con datos: firstName={}, lastName={}, dni={}",
                request.firstName(), request.lastName(), request.dni());

        VeterinaryResponseDTO veterinary = veterinaryService.register(request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Veterinario registrado exitosamente!")
                .status(HttpStatus.CREATED.value())
                .data(veterinary)
                .build();

        LOGGER.info("Registro exitoso. Nuevo veterinario ID: {}", veterinary.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateVeterinary(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid VeterinaryCreateRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updateVeterinary() con ID: {} y datos: firstName={}, lastName={}, dni={}",
                id, request.firstName(), request.lastName(), request.dni());

        VeterinaryResponseDTO veterinary = veterinaryService.update(id, request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Veterinario actualizado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(veterinary)
                .build();

        LOGGER.info("Actualización exitosa. Veterinario ID: {}", veterinary.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteVeterinary(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método deleteVeterinary() con ID: {}", id);
        veterinaryService.delete(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Veterinario eliminado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

        LOGGER.info("Eliminación exitosa. Veterinario ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
