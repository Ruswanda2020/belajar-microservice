package com.onedev.customer_service.controller;

import com.onedev.customer_service.dto.CustomerRequest;
import com.onedev.customer_service.dto.CustomerResponse;
import com.onedev.customer_service.dto.response.DataResponse;
import com.onedev.customer_service.dto.response.DatatableResponse;
import com.onedev.customer_service.dto.response.StandardResponse;
import com.onedev.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StandardResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponse.builder()
                        .result("success")
                        .detail("Customer created successfully")
                        .path("/api/customer")
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.CREATED.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DatatableResponse<CustomerResponse>> getAllCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        DatatableResponse<CustomerResponse> customers = customerService.getAllCustomers(page, limit, sortField, sortOrder);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DataResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(
                new DataResponse<>(
                        "success",
                        "Customer retrieved successfully",
                        "/api/customer/" + id,
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        HttpStatus.OK.value(),
                        "1.0",
                        Collections.emptyList(),
                        customer
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StandardResponse> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
        customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(
                StandardResponse.builder()
                        .result("success")
                        .detail("Customer updated successfully")
                        .path("/api/customer/" + id)
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.OK.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<StandardResponse> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponse.builder()
                        .result("success")
                        .detail("Customer deleted successfully")
                        .path("/api/customer/" + id)
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.NO_CONTENT.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }
}