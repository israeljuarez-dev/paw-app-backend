package com.veterinary.paw.mapper;

import com.veterinary.paw.domain.*;
import com.veterinary.paw.dto.request.VeterinaryAppointmentCreateRequestDTO;
import com.veterinary.paw.dto.request.appointment.VeterinaryAppointmentRegisterRequestDTO;
import com.veterinary.paw.dto.response.VeterinaryAppointmentCreateResponseDTO;
import com.veterinary.paw.dto.response.VeterinaryAppointmentResponseDTO;
import com.veterinary.paw.enums.AppointmentStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VeterinaryAppointmentMapper {

    private final PetMapper petMapper;

    private final VeterinaryMapper veterinaryMapper;

    private final VeterinaryServiceMapper veterinaryServiceMapper;

    private final ShiftMapper shiftMapper;

    // El método que crea la respuesta detallada y rompe la recursividad
    public VeterinaryAppointmentResponseDTO toResponseDTO(VeterinaryAppointment appointment) {
        return VeterinaryAppointmentResponseDTO.builder()
                .id(appointment.getId())
                .registerDate(appointment.getRegisterDate())
                .status(appointment.getStatus())
                .observations(appointment.getObservations())
                // Llamadas a mappers que devuelven DTOs simplificados (sin dependencias circulares)
                .pet(petMapper.toResponseDTO(appointment.getPet()))
                .veterinary(veterinaryMapper.toResponseDTO(appointment.getVeterinary()))
                .veterinaryService(veterinaryServiceMapper.toResponseDTO(appointment.getVeterinaryService()))
                .shift(shiftMapper.toResponseDTO(appointment.getShift()))
                .build();
    }

    // Método toEntity (Se mantiene como estaba)
    public VeterinaryAppointment toEntity(
            VeterinaryAppointmentRegisterRequestDTO request,
            Pet pet,
            Veterinary veterinary,
            VeterinaryService veterinaryService,
            Shift shift
    ) {

        return VeterinaryAppointment.builder()
                .veterinaryService(veterinaryService)
                .shift(shift)
                .pet(pet)
                .veterinary(veterinary)
                .observations(request.observations())
                .status(AppointmentStatusEnum.PENDIENTE)
                .registerDate(LocalDate.now())
                .build();
    }
}
