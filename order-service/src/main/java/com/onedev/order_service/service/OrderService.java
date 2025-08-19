package com.onedev.order_service.service;

import com.onedev.order_service.dto.OrderRequest;
import com.onedev.order_service.dto.OrderResponse;
import com.onedev.order_service.entity.Order;
import com.onedev.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import com.onedev.order_service.dto.response.DatatableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public void createOrder(OrderRequest orderRequest) {
        // Call Customer Service to validate customerId
        ResponseEntity<Void> customerResponse = restTemplate.getForEntity(
                "http://CUSTOMER-SERVICE/api/customer/" + orderRequest.getCustomerId(), Void.class);

        if (customerResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Customer not found with ID: " + orderRequest.getCustomerId());
        }

        // Call Product Service to validate productId
        ResponseEntity<Void> productResponse = restTemplate.getForEntity(
                "http://PRODUCT-SERVICE/api/product/" + orderRequest.getProductId(), Void.class);

        if (productResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Product not found with ID: " + orderRequest.getProductId());
        }

        Order order = Order.builder()
                .customerId(orderRequest.getCustomerId())
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderDate(orderRequest.getOrderDate())
                .build();

        orderRepository.save(order);
    }

    public DatatableResponse<OrderResponse> getAllOrders(int page, int limit, String sortField, String sortOrder) {
        try {
            String[] allowedOrder = {"id", "customerId", "productId", "quantity", "orderDate"}; // Define allowed sort fields
            Sort sort = Sort.by(Sort.Direction.ASC, "id"); // Default sort

            if (Arrays.asList(allowedOrder).contains(sortField)) {
                sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(Sort.Direction.ASC, sortField) : Sort.by(Sort.Direction.DESC, sortField);
            }

            Pageable pageable = PageRequest.of(page - 1, limit, sort);
            Page<Order> pageResult = orderRepository.findAll(pageable);

            return toDatatableResponse(pageResult, page, limit, this::mapToOrderResponse);
        } catch (Exception e) {
            // Log the error
            throw new RuntimeException("Error while getting paginated orders: " + e.getMessage(), e);
        }
    }

    private DatatableResponse<OrderResponse> toDatatableResponse(Page<Order> pageResult, int page, int limit, Function<Order, OrderResponse> mapper) {
        List<OrderResponse> content = pageResult.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return DatatableResponse.<OrderResponse>builder()
                .data(content)
                .recordsTotal(pageResult.getTotalElements())
                .recordsFiltered(pageResult.getTotalElements()) // For simple cases, recordsFiltered can be same as recordsTotal
                .page(page)
                .limit(limit)
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .orderDate(order.getOrderDate())
                .build();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToOrderResponse(order);
    }

    public void updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setCustomerId(orderRequest.getCustomerId());
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderDate(orderRequest.getOrderDate());

        orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}