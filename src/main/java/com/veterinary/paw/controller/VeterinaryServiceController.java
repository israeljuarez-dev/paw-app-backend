package com.veterinary.paw.controller;

import com.veterinary.paw.dto.request.VeterinaryServiceCreateRequestDTO;
import com.veterinary.paw.dto.response.ResponseDTO;
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
    public ResponseEntity<ResponseDTO> getServiceById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getServiceById() con ID: {}", id);
        VeterinaryServiceResponseDTO service = veterinaryServiceService.getById(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Servicio obtenido exitosamente!")
                .status(HttpStatus.OK.value())
                .data(service)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getServices() {
        LOGGER.info("Ingresando al método getServices() para obtener todos los servicios");
        List<VeterinaryServiceResponseDTO> services = veterinaryServiceService.get();

        if (services.isEmpty()) {
            LOGGER.warn("No se encontraron servicios veterinarios registrados");
            return ResponseEntity.noContent().build();
        }

        ResponseDTO response = ResponseDTO.builder()
                .message("Servicios obtenidos exitosamente!")
                .status(HttpStatus.OK.value())
                .data(services)
                .build();

        LOGGER.info("Se encontraron {} servicios veterinarios registrados", services.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> registerService(
            @RequestBody @Valid VeterinaryServiceCreateRequestDTO request
    ){
        LOGGER.info("➡Ingresando al método registerService() con datos: name={}, price={}",
                request.name(), request.price());

        VeterinaryServiceResponseDTO service = veterinaryServiceService.register(request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Servicio registrado exitosamente!")
                .status(HttpStatus.CREATED.value())
                .data(service)
                .build();

        LOGGER.info("Registro exitoso. Nuevo servicio ID: {}", service.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateService(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid VeterinaryServiceCreateRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updateService() con ID: {} y datos: name={}, price={}",
                id, request.name(), request.price());

        VeterinaryServiceResponseDTO service = veterinaryServiceService.update(id, request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Servicio actualizado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(service)
                .build();

        LOGGER.info("Actualización exitosa. Servicio ID: {}", service.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteService(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método deleteService() con ID: {}", id);
        veterinaryServiceService.delete(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Servicio eliminado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

        LOGGER.info("Eliminación exitosa. Servicio ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
