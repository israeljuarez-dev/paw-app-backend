package com.veterinary.paw.controller;

import com.veterinary.paw.dto.request.CustomerCreateRequestDTO;
import com.veterinary.paw.dto.response.CustomerResponseDTO;
import com.veterinary.paw.dto.response.ResponseDTO;
import com.veterinary.paw.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getCustomerById(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método getCustomerById() con ID: {}", id);
        CustomerResponseDTO customer = customerService.getById(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cliente obtenido exitosamente!")
                .status(HttpStatus.OK.value())
                .data(customer)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getCustomers() {
        LOGGER.info("Ingresando al método getCustomers() para obtener todos los clientes");
        List<CustomerResponseDTO> customers = customerService.get();
        if (customers.isEmpty()) {
            LOGGER.warn("No se encontraron clientes registrados");
            return ResponseEntity.noContent().build();
        }

        ResponseDTO response = ResponseDTO.builder()
                .message("Clientes obtenidos exitosamente!")
                .status(HttpStatus.OK.value())
                .data(customers)
                .build();

        LOGGER.info("Se encontraron {} clientes registrados", customers.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> registerCustomer(
            @RequestBody @Valid CustomerCreateRequestDTO request
    ){
        LOGGER.info("➡Ingresando al método registerCustomer() con datos: firstName={}, lastName={}, dni={}",
                request.firstName(), request.lastName(), request.dni());

        CustomerResponseDTO customer = customerService.register(request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cliente registrado exitosamente!")
                .status(HttpStatus.CREATED.value())
                .data(customer)
                .build();

        LOGGER.info("Registro exitoso. Nuevo cliente ID: {}", customer.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateCustomer(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid CustomerCreateRequestDTO request
    ) {
        LOGGER.info("Ingresando al método updateCustomer() con ID: {} y datos: firstName={}, lastName={}, dni={}",
                id, request.firstName(), request.lastName(), request.dni());

        CustomerResponseDTO customer = customerService.update(id, request);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cliente actualizado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(customer)
                .build();

        LOGGER.info("Actualización exitosa. Cliente ID: {}", customer.id());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteCustomer(@PathVariable @Min(1) Long id) {
        LOGGER.info("Ingresando al método deleteCustomer() con ID: {}", id);
        customerService.delete(id);

        ResponseDTO response = ResponseDTO.builder()
                .message("Cliente eliminado exitosamente!")
                .status(HttpStatus.OK.value())
                .data(null)
                .build();

        LOGGER.info("Eliminación exitosa. Cliente ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
