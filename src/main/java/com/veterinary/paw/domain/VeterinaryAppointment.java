package com.veterinary.paw.domain;

import com.veterinary.paw.enums.AppointmentStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Table(name = "veterinary_appointment")
public class VeterinaryAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "observations")
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private AppointmentStatusEnum status = AppointmentStatusEnum.PENDIENTE;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @ManyToOne(
            targetEntity = Pet.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id_pet",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pet_appointment")
    )
    private Pet pet;

    @ManyToOne(
            targetEntity = Veterinary.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id_veterinary",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_veterinary_appointment")
    )
    private Veterinary veterinary;

    @ManyToOne(
            targetEntity = VeterinaryService.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id_veterinary_service",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_veterinary_service_appointment")
    )
    private VeterinaryService veterinaryService;

    @ManyToOne(
            targetEntity = Shift.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id_shift",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_shift_appointment")
    )
    private Shift shift;
}
