package com.veterinary.paw.controller;

import com.veterinary.paw.domain.VeterinaryAppointment;
import com.veterinary.paw.dto.criteria.appointment.SearchVeterinaryAppointmentCriteriaDTO;
import com.veterinary.paw.dto.request.VeterinaryAppointmentCreateRequestDTO;
import com.veterinary.paw.dto.request.appointment.VeterinaryAppointmentRegisterRequestDTO;
import com.veterinary.paw.dto.response.ResponseDTO;
import com.veterinary.paw.dto.response.VeterinaryAppointmentCreateResponseDTO;
import com.veterinary.paw.dto.response.VeterinaryAppointmentResponseDTO;
import com.veterinary.paw.service.VeterinaryAppointmentService;
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
@Validated
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class VeterinaryAppointmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VeterinaryAppointmentController.class);

    private final VeterinaryAppointmentService veterinaryAppointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getAppointmentById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getAppointmentById() con ID: {}", id);
        VeterinaryAppointmentResponseDTO appointment = veterinaryAppointmentService.getById(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cita obtenida exitosamente!")
                .status(HttpStatus.OK.value())
                .data(appointment)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAppointments(SearchVeterinaryAppointmentCriteriaDTO criteriaDTO) {
        LOGGER.info("Ingresando al método getAppointments() para obtener todas las citas veterinarias");
        List<VeterinaryAppointmentResponseDTO> appointments = veterinaryAppointmentService.get(criteriaDTO);
        if (appointments.isEmpty()) {
            LOGGER.warn("No se encontraron citas registradas");
            return ResponseEntity.noContent().build();
        }

        ResponseDTO response = ResponseDTO.builder()
                .message("Citas obtenidas exitosamente!")
                .status(HttpStatus.OK.value())
                .data(appointments)
                .build();

        LOGGER.info("Se encontraron {} citas registradas", appointments.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerVeterinaryAppointment(@RequestBody @Valid VeterinaryAppointmentRegisterRequestDTO request){
        LOGGER.info("Ingresando al método registerVeterinaryAppointment()");
        VeterinaryAppointmentResponseDTO appointmentResponseDTO = veterinaryAppointmentService.registerAppointment(request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cita registrada exitosamente!")
                .status(HttpStatus.CREATED.value())
                .data(appointmentResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}") // O @PatchMapping si solo esperas campos parciales
    public ResponseEntity<ResponseDTO> updateVeterinaryAppointment(
            @PathVariable Long id,
            @RequestBody @Valid VeterinaryAppointmentRegisterRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updateVeterinaryAppointment() para ID: {}", id);

        VeterinaryAppointmentResponseDTO appointmentResponseDTO = veterinaryAppointmentService.update(id, request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cita actualizada exitosamente!")
                .status(HttpStatus.OK.value())
                .data(appointmentResponseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteAppointment(@PathVariable Long id) {
        LOGGER.info("Ingresando al método deleteAppointment() con ID: {}", id);
        veterinaryAppointmentService.delete(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cita eliminada exitosamente!")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
