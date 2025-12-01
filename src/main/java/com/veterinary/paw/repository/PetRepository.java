package com.veterinary.paw.repository;

import com.veterinary.paw.domain.Customer;
import com.veterinary.paw.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> findByFirstNameAndLastNameAndOwner(String petFirstName, String petLastName, Customer customer);
}
