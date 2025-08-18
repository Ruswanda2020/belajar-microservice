package com.onedev.customer_service.service;

import com.onedev.customer_service.dto.CustomerRequest;
import com.onedev.customer_service.dto.CustomerResponse;
import com.onedev.customer_service.entity.Customer;
import com.onedev.customer_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import com.onedev.customer_service.dto.response.DatatableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public void createCustomer(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .email(customerRequest.getEmail())
                .address(customerRequest.getAddress())
                .build();

        customerRepository.save(customer);
    }

    public DatatableResponse<CustomerResponse> getAllCustomers(int page, int limit, String sortField, String sortOrder) {
        try {
            String[] allowedOrder = {"id", "name", "email", "address"}; // Define allowed sort fields
            Sort sort = Sort.by(Sort.Direction.ASC, "id"); // Default sort

            if (Arrays.asList(allowedOrder).contains(sortField)) {
                sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(Sort.Direction.ASC, sortField) : Sort.by(Sort.Direction.DESC, sortField);
            }

            Pageable pageable = PageRequest.of(page - 1, limit, sort);
            Page<Customer> pageResult = customerRepository.findAll(pageable);

            return toDatatableResponse(pageResult, page, limit, this::mapToCustomerResponse);
        } catch (Exception e) {
            // Log the error
            throw new RuntimeException("Error while getting paginated customers: " + e.getMessage(), e);
        }
    }

    private DatatableResponse<CustomerResponse> toDatatableResponse(Page<Customer> pageResult, int page, int limit, java.util.function.Function<Customer, CustomerResponse> mapper) {
        List<CustomerResponse> content = pageResult.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return DatatableResponse.<CustomerResponse>builder()
                .data(content)
                .recordsTotal(pageResult.getTotalElements())
                .recordsFiltered(pageResult.getTotalElements()) // For simple cases, recordsFiltered can be same as recordsTotal
                .page(page)
                .limit(limit)
                .build();
    }

    private CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToCustomerResponse(customer);
    }

    public void updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());

        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}