package com.veterinary.paw.dto.criteria.appointment;

import lombok.*;

@Data
@Builder
public class SearchVeterinaryAppointmentCriteriaDTO {

    String sortField; //campo a ordenar

    String sortingDirection; //direccion de ordenamiento, ascendente o descendente

    Integer pageActual = 0; //representa la página en la que se encuentra

    Integer pageSize = 10; //representa la cantidad de registros se mostrarán por página
}
