package com.onedev.order_service.controller;

import com.onedev.order_service.dto.OrderRequest;
import com.onedev.order_service.dto.OrderResponse;
import com.onedev.order_service.dto.response.DataResponse;
import com.onedev.order_service.dto.response.DatatableResponse;
import com.onedev.order_service.dto.response.StandardResponse;
import com.onedev.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StandardResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponse.builder()
                        .result("success")
                        .detail("Order created successfully")
                        .path("/api/order")
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.CREATED.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DatatableResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        DatatableResponse<OrderResponse> orders = orderService.getAllOrders(page, limit, sortField, sortOrder);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DataResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(
                new DataResponse<>(
                        "success",
                        "Order retrieved successfully",
                        "/api/order/" + id,
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        HttpStatus.OK.value(),
                        "1.0",
                        Collections.emptyList(),
                        order
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StandardResponse> updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        orderService.updateOrder(id, orderRequest);
        return ResponseEntity.ok(
                StandardResponse.builder()
                        .result("success")
                        .detail("Order updated successfully")
                        .path("/api/order/" + id)
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.OK.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<StandardResponse> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponse.builder()
                        .result("success")
                        .detail("Order deleted successfully")
                        .path("/api/order/" + id)
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.NO_CONTENT.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }
}