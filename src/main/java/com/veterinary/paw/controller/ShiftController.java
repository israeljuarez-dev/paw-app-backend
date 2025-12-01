package com.veterinary.paw.controller;

import com.veterinary.paw.dto.request.ShiftCreateRequestDTO;
import com.veterinary.paw.dto.response.ResponseDTO;
import com.veterinary.paw.dto.response.ShiftResponseDTO;
import com.veterinary.paw.service.ShiftService;
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
@RequestMapping("/shifts")
@RequiredArgsConstructor
@Validated
public class ShiftController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiftController.class);

    private final ShiftService shiftService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getShiftById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getShiftById() con ID: {}", id);
        ShiftResponseDTO shift = shiftService.getById(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Turno obtenido exitosamente!")
                .status(HttpStatus.OK.value())
                .data(shift)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getShifts() {
        LOGGER.info("Ingresando al método getShifts() para obtener todos los turnos");
        List<ShiftResponseDTO> shifts = shiftService.get();
        if (shifts.isEmpty()) {
            LOGGER.warn("No se encontraron turnos registrados");
            return ResponseEntity.noContent().build();
        }

        ResponseDTO response = ResponseDTO.builder()
                .message("Turnos obtenidos exitosamente!")
                .status(HttpStatus.OK.value())
                .data(shifts)
                .build();

        LOGGER.info("Se encontraron {} turnos registrados", shifts.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> registerShift(
            @RequestBody @Valid ShiftCreateRequestDTO request
    ){
        LOGGER.info("➡Ingresando al método registerShift() con datos: date={}, startTime={}, veterinaryId={}",
                request.date(), request.startTime(), request.veterinaryId());

        ShiftResponseDTO shift = shiftService.register(request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Turno registrado exitosamente!")
                .status(HttpStatus.CREATED.value())
                .data(shift)
                .build();

        LOGGER.info("Registro exitoso. Nuevo turno ID: {}", shift.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateShift(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid ShiftCreateRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updateShift() con ID: {} y datos: date={}, startTime={}, veterinaryId={}",
                id, request.date(), request.startTime(), request.veterinaryId());

        ShiftResponseDTO shift = shiftService.update(id, request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Turno actualizado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(shift)
                .build();

        LOGGER.info("Actualización exitosa. Turno ID: {}", shift.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteShift(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método deleteShift() con ID: {}", id);
        shiftService.delete(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Turno eliminado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

        LOGGER.info("Eliminación exitosa. Turno ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
