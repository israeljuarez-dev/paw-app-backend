package com.veterinary.paw.repository;

import com.veterinary.paw.domain.VeterinaryAppointment;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VeterinaryAppointmentRepository extends JpaRepository<VeterinaryAppointment, Long> {

    @Transactional(readOnly = true)
    List<VeterinaryAppointment> findAll(Specification<VeterinaryAppointment> specification, Pageable pageable);
}
