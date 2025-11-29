package com.veterinary.paw.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "dni", length = 20, nullable = false, unique = true)
    private String dni;

    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Pet> pets;
}
