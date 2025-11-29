package com.veterinary.paw.repository;

import com.veterinary.paw.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByDni(String dni);
}
