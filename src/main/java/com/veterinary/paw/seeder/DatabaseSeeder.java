package com.veterinary.paw.seeder;

import com.veterinary.paw.domain.*;
import com.veterinary.paw.enums.PetGenderEnum;
import com.veterinary.paw.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class DatabaseSeeder implements CommandLineRunner {

    private final UserEntityRepository userEntityRepository;

    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    private final ShiftRepository shiftRepository;

    private final VeterinaryServiceRepository veterinaryServiceRepository;

    private final VeterinaryRepository veterinaryRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // --- 1. USUARIOS (USERS) ---
        if (userEntityRepository.count() == 0) {
            UserEntity user1 = UserEntity.builder()
                    .email("israel.juarez@gmail.com")
                    .password(passwordEncoder.encode("israeljuarez1218"))
                    .dni("26582018")
                    .phoneNumber("974459640")
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .email("lara@gmail.com")
                    .password(passwordEncoder.encode("lara123"))
                    .dni("76889526")
                    .phoneNumber("923459989")
                    .build();

            userEntityRepository.saveAll(Arrays.asList(user1, user2));
            System.out.println("✅ 2 Usuarios insertados.");
        }

        // --- 2. VETERINARIOS (VETERINARY) ---
        if (veterinaryRepository.count() == 0) {
            Veterinary vet1 = Veterinary.builder()
                    .firstName("Ana")
                    .lastName("García")
                    .birthDate(LocalDate.of(1985, 5, 10))
                    .speciality("Cardiología")
                    .phoneNumber("911223344")
                    .email("ana.garcia@vet.com")
                    .dni("10010010")
                    .build();

            Veterinary vet2 = Veterinary.builder()
                    .firstName("Marcos")
                    .lastName("López")
                    .birthDate(LocalDate.of(1990, 8, 20))
                    .speciality("Cirugía")
                    .phoneNumber("922334455")
                    .email("marcos.lopez@vet.com")
                    .dni("20020020")
                    .build();

            veterinaryRepository.saveAll(Arrays.asList(vet1, vet2));
            System.out.println("✅ 2 Veterinarios insertados.");
        }

        // --- 3. SERVICIOS VETERINARIOS (VETERINARY SERVICE) ---
        if (veterinaryServiceRepository.count() == 0) {
            VeterinaryService service1 = VeterinaryService.builder()
                    .name("Consulta General")
                    .description("Revisión de rutina y chequeo de salud.")
                    .price(new BigDecimal("35.00"))
                    .build();

            VeterinaryService service2 = VeterinaryService.builder()
                    .name("Vacunación Anual")
                    .description("Administración de vacunas anuales requeridas.")
                    .price(new BigDecimal("25.50"))
                    .build();

            veterinaryServiceRepository.saveAll(Arrays.asList(service1, service2));
            System.out.println("✅ 2 Servicios insertados.");
        }

        // --- 4. TURNOS (SHIFTS) ---
        // Requiere Veterinarios
        if (shiftRepository.count() == 0) {

            // Se asume que vet1 es el primero guardado
            Veterinary vetA = veterinaryRepository.findAll().get(0);

            Shift shift1 = Shift.builder()
                    .date(LocalDate.now().plusDays(2))
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(13, 0))
                    .available(true)
                    .veterinary(vetA)
                    .build();

            Shift shift2 = Shift.builder()
                    .date(LocalDate.now().plusDays(3))
                    .startTime(LocalTime.of(14, 0))
                    .endTime(LocalTime.of(18, 0))
                    .available(true)
                    .veterinary(vetA)
                    .build();

            Shift shift3 = Shift.builder()
                    .date(LocalDate.now().plusDays(4))
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(10, 0))
                    .available(true)
                    .veterinary(vetA)
                    .build();

            Shift shift4 = Shift.builder()
                    .date(LocalDate.now().plusDays(5))
                    .startTime(LocalTime.of(11, 0))
                    .endTime(LocalTime.of(12, 0))
                    .available(true)
                    .veterinary(vetA)
                    .build();

            shiftRepository.saveAll(Arrays.asList(shift1, shift2, shift3, shift4));


            System.out.println("✅ 4 Turnos insertados.");
        }

        // --- 5. CLIENTES (CUSTOMERS) ---
        // Se insertan antes de las mascotas
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .firstName("Israel")
                    .lastName("Juárez")
                    .dni("75889126")
                    .phoneNumber("975168852")
                    .email("israel.juarez.client@mail.com")
                    .build();

            Customer customer2 = Customer.builder()
                    .firstName("Juan")
                    .lastName("Torres")
                    .dni("03882199")
                    .phoneNumber("923562889")
                    .email("juan.torres.client@mail.com")
                    .build();

            customerRepository.saveAll(Arrays.asList(customer1, customer2));
            System.out.println("✅ 2 Clientes insertados.");
        }

        // --- 6. MASCOTAS (PETS) ---
        // Requiere Clientes (Owner)
        if (petRepository.count() == 0) {
            List<Customer> customers = customerRepository.findAll();
            Customer owner1 = customers.get(0);
            Customer owner2 = customers.get(1);

            Pet pet1 = Pet.builder()
                    .firstName("Bella")
                    .lastName("Juárez")
                    .age(4)
                    .birthDate(LocalDate.of(2021, 10, 15))
                    .specie("Perro")
                    .gender(PetGenderEnum.FEMALE)
                    .owner(owner1)
                    .build();

            Pet pet2 = Pet.builder()
                    .firstName("Milo")
                    .lastName("Torres")
                    .age(2)
                    .birthDate(LocalDate.of(2023, 1, 25))
                    .specie("Gato")
                    .gender(PetGenderEnum.MALE)
                    .owner(owner2)
                    .build();

            petRepository.saveAll(Arrays.asList(pet1, pet2));
            System.out.println("✅ 2 Mascotas insertadas.");
        }
    }
}
