package com.veterinary.paw.controller;

import com.veterinary.paw.dto.request.PetCreateRequestDTO;
import com.veterinary.paw.dto.response.PetResponseDTO;
import com.veterinary.paw.dto.response.ResponseDTO;
import com.veterinary.paw.service.PetService;
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
@RequestMapping("/pets")
@RequiredArgsConstructor
@Validated
public class PetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getPetById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getPetById() con ID: {}", id);
        PetResponseDTO pet = petService.getById(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Mascota obtenida exitosamente!")
                .status(HttpStatus.OK.value())
                .data(pet)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getPets() {
        LOGGER.info("Ingresando al método getPets() para obtener todas las mascotas");
        List<PetResponseDTO> pets = petService.get();
        if (pets.isEmpty()) {
            LOGGER.warn("No se encontraron mascotas registradas");
            return ResponseEntity.noContent().build();
        }

        ResponseDTO response = ResponseDTO.builder()
                .message("Mascotas obtenidas exitosamente!")
                .status(HttpStatus.OK.value())
                .data(pets)
                .build();

        LOGGER.info("Se encontraron {} mascotas registradas", pets.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> registerPet(
            @RequestBody @Valid PetCreateRequestDTO request
    ){
        LOGGER.info("➡Ingresando al método registerPet() con datos: firstName={}, specie={}, ownerId={}",
                request.firstName(), request.specie(), request.ownerId());

        PetResponseDTO pet = petService.register(request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Mascota registrada exitosamente!")
                .status(HttpStatus.CREATED.value())
                .data(pet)
                .build();

        LOGGER.info("Registro exitoso. Nueva mascota ID: {}", pet.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updatePet(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid PetCreateRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updatePet() con ID: {} y datos: firstName={}, specie={}, ownerId={}",
                id, request.firstName(), request.specie(), request.ownerId());

        PetResponseDTO pet = petService.update(id, request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Mascota actualizada exitosamente!")
                .status(HttpStatus.OK.value())
                .data(pet)
                .build();

        LOGGER.info("Actualización exitosa. Mascota ID: {}", pet.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deletePet(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método deletePet() con ID: {}", id);
        petService.delete(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Mascota eliminada exitosamente!")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

        LOGGER.info("Eliminación exitosa. Mascota ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
