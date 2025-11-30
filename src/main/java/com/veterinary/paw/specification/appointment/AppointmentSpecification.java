package com.veterinary.paw.specification.appointment;

import com.veterinary.paw.domain.VeterinaryAppointment;
import com.veterinary.paw.dto.criteria.appointment.SearchVeterinaryAppointmentCriteriaDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecification {

    public static Specification<VeterinaryAppointment> withSearchCriteria(SearchVeterinaryAppointmentCriteriaDTO criteriaDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteriaDTO.getSortingDirection() != null && criteriaDTO.getSortField() != null) {
                if (criteriaDTO.getSortingDirection().equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(criteriaDTO.getSortField())));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(criteriaDTO.getSortField())));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
