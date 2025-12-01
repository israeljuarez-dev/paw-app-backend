package com.veterinary.paw.service;

import com.veterinary.paw.domain.*;
import com.veterinary.paw.domain.VeterinaryService;
import com.veterinary.paw.dto.criteria.appointment.SearchVeterinaryAppointmentCriteriaDTO;
import com.veterinary.paw.dto.request.VeterinaryAppointmentCreateRequestDTO;
import com.veterinary.paw.dto.request.appointment.CustomerInfoDTO;
import com.veterinary.paw.dto.request.appointment.PetInfoDTO;
import com.veterinary.paw.dto.request.appointment.VeterinaryAppointmentRegisterRequestDTO;
import com.veterinary.paw.dto.response.VeterinaryAppointmentCreateResponseDTO;
import com.veterinary.paw.dto.response.VeterinaryAppointmentResponseDTO;
import com.veterinary.paw.enums.ApiErrorEnum;
import com.veterinary.paw.enums.AppointmentStatusEnum;
import com.veterinary.paw.enums.PetGenderEnum;
import com.veterinary.paw.exception.PawException;
import com.veterinary.paw.mapper.VeterinaryAppointmentMapper;
import com.veterinary.paw.repository.*;
import com.veterinary.paw.specification.appointment.AppointmentSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinaryAppointmentService {

    private static Logger LOGGER = LoggerFactory.getLogger(VeterinaryAppointmentService.class);

    private final VeterinaryAppointmentRepository veterinaryAppointmentRepository;

    private final PetRepository petRepository;

    private final ShiftRepository shiftRepository;

    private final VeterinaryRepository veterinaryRepository;

    private final VeterinaryServiceRepository veterinaryServiceRepository;

    private final VeterinaryAppointmentMapper veterinaryAppointmentMapper;

    private final CustomerRepository customerRepository;


    @Transactional(readOnly = true)
    public List<VeterinaryAppointmentResponseDTO> get(SearchVeterinaryAppointmentCriteriaDTO criteriaDTO){
        Pageable pageable = PageRequest.of(criteriaDTO.getPageActual(), criteriaDTO.getPageSize());

        List<VeterinaryAppointment> appointments = veterinaryAppointmentRepository.findAll(AppointmentSpecification.withSearchCriteria(criteriaDTO), pageable);

        if (appointments.isEmpty()) {
            LOGGER.warn("Appointment are empty list.");
            return Collections.emptyList();
        }

        return appointments.stream()
                .map(veterinaryAppointmentMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeterinaryAppointmentResponseDTO getById(Long id){
        VeterinaryAppointment appointment = veterinaryAppointmentRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Cita no encontrada. ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_APPOINTMENT_NOT_FOUND);
                    });

        return veterinaryAppointmentMapper.toResponseDTO(appointment);
    }

    /*
    @Transactional
    public VeterinaryAppointmentCreateResponseDTO register(VeterinaryAppointmentCreateRequestDTO request) {
        Pet pet = findPet(request.idPet());
        Veterinary veterinary = findVeterinary(request.idVeterinary());
        com.veterinary.paw.domain.VeterinaryService service = findVeterinaryService(request.idVeterinaryService());
        Shift shift = findShift(request.idShift());

        validateAppointmentDateIsFuture(shift.getDate());

        List<Shift> veterinaryShifts = getShiftsForVeterinarianOnDate(veterinary.getId(),shift.getDate());
        Shift coincidentShift = validateShiftTimes(shift, veterinaryShifts);

        List<Shift> reservedShifts = shiftRepository.findReservedShiftsByVeterinaryIdAndDate(veterinary.getId(), shift.getDate());
        validateShiftConflicts(coincidentShift , reservedShifts);

        VeterinaryAppointment appointment = VeterinaryAppointment.builder()
                .status(request.status())
                .observations(request.observations())
                .registerDate(LocalDate.now())
                .pet(pet)
                .veterinary(veterinary)
                .veterinaryService(service)
                .shift(shift)
                .build();

        veterinaryAppointmentRepository.save(appointment);

        shift.setAvailable(false);
        shiftRepository.save(shift);

        LOGGER.info("Cita registrada correctamente. ID cita: {}, Mascota: {}, Veterinario: {}, Fecha: {}",
                appointment.getId(), pet.getId(), veterinary.getId(), shift.getDate());

        return veterinaryAppointmentMapper.toCreateResponseDTO(appointment);
    }*/

    @Transactional
    public VeterinaryAppointmentResponseDTO registerAppointment(VeterinaryAppointmentRegisterRequestDTO registerRequestDTO) {
        CustomerInfoDTO customerInfo = registerRequestDTO.customerInfo();
        Customer customer = customerRepository.findByDni(customerInfo.customerDni())
                .orElseGet(() -> Customer.builder()
                        .dni(customerInfo.customerDni())
                        .firstName(customerInfo.customerFirstName())
                        .lastName(customerInfo.customerLastName())
                        .phoneNumber(customerInfo.customerPhoneNumber())
                        .email(customerInfo.customerEmail())
                        .build());

        customerRepository.save(customer);

        PetInfoDTO petInfo = registerRequestDTO.petInfo();
        Pet pet = petRepository.findByFirstNameAndLastNameAndOwner(petInfo.petFirstName(), petInfo.petLastName(), customer)
                .orElseGet(() -> Pet.builder()
                        .firstName(petInfo.petFirstName())
                        .lastName(petInfo.petLastName())
                        .age(Period.between(petInfo.petBirthDate(), LocalDate.now()).getYears())
                        .gender(PetGenderEnum.valueOf(petInfo.petGender().toUpperCase()))
                        .specie(petInfo.petSpecie())
                        .birthDate(petInfo.petBirthDate())
                        .owner(customer)
                        .build());

        petRepository.save(pet);

        Veterinary veterinary = findVeterinary(registerRequestDTO.veterinaryId());

        com.veterinary.paw.domain.VeterinaryService service = findVeterinaryService(registerRequestDTO.veterinaryServiceId());

        Shift shift = findShift(registerRequestDTO.shiftId());

        validateAppointmentDateIsFuture(shift.getDate());

        List<Shift> veterinaryShifts = getShiftsForVeterinarianOnDate(veterinary.getId(),shift.getDate());

        Shift coincidentShift = validateShiftTimes(shift, veterinaryShifts);

        List<Shift> reservedShifts = shiftRepository.findReservedShiftsByVeterinaryIdAndDate(veterinary.getId(), shift.getDate());

        validateShiftConflicts(coincidentShift , reservedShifts);


        VeterinaryAppointment appointment = VeterinaryAppointment.builder()
                .observations(registerRequestDTO.observations())
                .registerDate(LocalDate.now())
                .status(AppointmentStatusEnum.PENDIENTE)
                .pet(pet)
                .veterinary(veterinary)
                .veterinaryService(service)
                .shift(shift)
                .build();

        shift.setAvailable(false);
        shiftRepository.save(shift);

        VeterinaryAppointment savedAppointment = veterinaryAppointmentRepository.save(appointment);

        return veterinaryAppointmentMapper.toResponseDTO(savedAppointment);
    }

    @Transactional
    public VeterinaryAppointmentResponseDTO update(Long id, VeterinaryAppointmentRegisterRequestDTO request) {
        // 1. Verificar si la cita existe
        VeterinaryAppointment existingAppointment = veterinaryAppointmentRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.error("Cita veterinaria no encontrada para actualizar. ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_APPOINTMENT_NOT_FOUND);
                });

        // 2. Lógica de Customer (Unificación con register)
        CustomerInfoDTO customerInfo = request.customerInfo();
        Customer customer = customerRepository.findByDni(customerInfo.customerDni())
                .orElseGet(() -> Customer.builder()
                        .dni(customerInfo.customerDni())
                        .firstName(customerInfo.customerFirstName())
                        .lastName(customerInfo.customerLastName())
                        .phoneNumber(customerInfo.customerPhoneNumber())
                        .email(customerInfo.customerEmail())
                        .build());
        customerRepository.save(customer);

        // 3. Lógica de Pet (Unificación con register)
        PetInfoDTO petInfo = request.petInfo();
        int age = Period.between(petInfo.petBirthDate(), LocalDate.now()).getYears();

        Pet pet = petRepository.findByFirstNameAndLastNameAndOwner(petInfo.petFirstName(), petInfo.petLastName(), customer)
                .orElseGet(() -> Pet.builder()
                        .firstName(petInfo.petFirstName())
                        .lastName(petInfo.petLastName())
                        .age(age)
                        .gender(PetGenderEnum.valueOf(petInfo.petGender().toUpperCase()))
                        .specie(petInfo.petSpecie())
                        .birthDate(petInfo.petBirthDate())
                        .owner(customer)
                        .build());
        petRepository.save(pet);


        // 4. Búsqueda de Entidades a actualizar
        Veterinary veterinary = findVeterinary(request.veterinaryId());
        com.veterinary.paw.domain.VeterinaryService service = findVeterinaryService(request.veterinaryServiceId());
        Shift newShift = findShift(request.shiftId());

        // 5. Validaciones de Shift (Unificación con register)
        validateAppointmentDateIsFuture(newShift.getDate());
        List<Shift> veterinaryShifts = getShiftsForVeterinarianOnDate(veterinary.getId(), newShift.getDate());
        Shift coincidentShift = validateShiftTimes(newShift, veterinaryShifts);

        // Al actualizar, el shift actual DEBE ser excluido de la validación de conflictos.
        // Asumo que tu findReservedShiftsByVeterinaryIdAndDate lo maneja o necesitas una lógica para excluir el shift original.
        List<Shift> reservedShifts = shiftRepository.findReservedShiftsByVeterinaryIdAndDate(veterinary.getId(), newShift.getDate());
        validateShiftConflicts(coincidentShift , reservedShifts);

        // 6. Manejo del Shift anterior
        Shift previousShift = existingAppointment.getShift();
        if (!previousShift.getId().equals(newShift.getId())) {
            // Si el turno cambió, liberamos el anterior
            previousShift.setAvailable(true);
            shiftRepository.save(previousShift);

            // El nuevo turno pasa a ser no disponible
            newShift.setAvailable(false);
            shiftRepository.save(newShift);
        }
        // NOTA: Si el shift no cambió, sus validaciones ya aseguran que está no disponible y reservado.

        // 7. Actualización de la Cita existente
        existingAppointment.setObservations(request.observations());
        existingAppointment.setRegisterDate(LocalDate.now()); // Registro de la actualización
        existingAppointment.setPet(pet);
        existingAppointment.setVeterinary(veterinary);
        existingAppointment.setVeterinaryService(service);
        existingAppointment.setShift(newShift); // Se asigna el nuevo (o el mismo) shift

        VeterinaryAppointment updatedAppointment = veterinaryAppointmentRepository.save(existingAppointment);

        LOGGER.info("Cita actualizada correctamente. ID cita: {}, Mascota: {}, Veterinario: {}, Fecha: {}",
                updatedAppointment.getId(), pet.getId(), veterinary.getId(), newShift.getDate());

        // 8. Devolver DTO de respuesta (Unificación con register)
        return veterinaryAppointmentMapper.toResponseDTO(updatedAppointment);
    }


    @Transactional
    public void delete(Long id){
        VeterinaryAppointment existingAppointment = veterinaryAppointmentRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Cita veterinaria no encontrada para eliminar. ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_APPOINTMENT_NOT_FOUND);
                });

        if (existingAppointment.getShift()!= null) {
            existingAppointment.getShift().setAvailable(true);
            shiftRepository.save(existingAppointment.getShift());
        }

        veterinaryAppointmentRepository.delete(existingAppointment);
    }

    private Pet findPet(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Mascota no encontrada. ID: {}", id);
                    return new PawException(ApiErrorEnum.PET_NOT_FOUND);
                });
    }

    private Veterinary findVeterinary(Long id) {
        return veterinaryRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Veterinario no encontrado. ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_NOT_FOUND);
                });
    }

    private com.veterinary.paw.domain.VeterinaryService findVeterinaryService(Long id) {
        return veterinaryServiceRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Servicio veterinario no encontrado. ID: {}", id);
                    return new PawException(ApiErrorEnum.VETERINARY_SERVICE_NOT_FOUND);
                });
    }

    private Shift findShift(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Turno no encontrado. ID: {}", id);
                    return new PawException(ApiErrorEnum.SHIFT_NOT_FOUND);
                });
    }

    private void validateAppointmentDateIsFuture(LocalDate date) {
        if (!date.isAfter(LocalDate.now())) {
            LOGGER.error("Fecha de turno inválida. Fecha: {}", date);
            throw new PawException(ApiErrorEnum.INVALID_SHIFT_DATE);
        }
    }

    private List<Shift> getShiftsForVeterinarianOnDate(Long veterinaryId, LocalDate appointmentDate){
        List<Shift> shifts = shiftRepository.findShiftByVeterinaryIdAndDate(veterinaryId, appointmentDate);
        if (shifts == null || shifts.isEmpty()) {
            LOGGER.error("El veterinario no trabaja en esta Fecha: {}", appointmentDate);
            throw new PawException(ApiErrorEnum.VETERINARY_NOT_AVAILABLE_THIS_DAY);
        }

        return shifts;
    }

    private Shift validateShiftTimes(Shift appointmentShift, List<Shift> veterinaryShifts){
        return veterinaryShifts.stream()
                .filter(s ->
                        !appointmentShift.getStartTime().isBefore(s.getStartTime()) &&
                        !appointmentShift.getEndTime().isAfter(s.getEndTime()))
                .findFirst()
                .orElseThrow(() -> {
                    LOGGER.error(
                            "No hay turnos del veterinario que coincidan con el horario solicitado: " +
                                    "Fechas del veterinario Veterinario ID: {}, " +
                                    "Fecha que se quiere reservar la cita: {}", veterinaryShifts, appointmentShift);
                    return new PawException(ApiErrorEnum.SHIFT_OUT_OF_WORKING_HOURS);
                });
    }

    private void validateShiftConflicts(Shift newAppointmentShift, List<Shift> existingShifts) {
        boolean hasConflict = existingShifts.stream()
                .anyMatch(s -> (
                                newAppointmentShift.getStartTime().isBefore(s.getEndTime()) &&
                                newAppointmentShift.getEndTime().isAfter(s.getStartTime())
                        )
                );

        if (hasConflict) {
            LOGGER.error(
                    "Conflicto de horario detectado para el veterinario ID {}: " +
                            "El turno solicitado ({}, {}) se superpone con un turno existente.",
                    newAppointmentShift.getVeterinary().getId(),
                    newAppointmentShift.getStartTime(),
                    newAppointmentShift.getEndTime()
            );

            throw new PawException(ApiErrorEnum.VETERINARY_SHIFT_CONFLICT);
        }
    }
}
