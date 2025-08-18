package com.onedev.product_service.controller;

import com.onedev.product_service.dto.ProductRequest;
import com.onedev.product_service.dto.ProductResponse;
import com.onedev.product_service.dto.response.DataResponse;
import com.onedev.product_service.dto.response.DatatableResponse;
import com.onedev.product_service.dto.response.StandardResponse;
import com.onedev.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StandardResponse> createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StandardResponse.builder()
                        .result("success")
                        .detail("Product created successfully")
                        .path("/api/product")
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.CREATED.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DatatableResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        DatatableResponse<ProductResponse> products = productService.getAllProducts(page, limit, sortField, sortOrder);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DataResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(
                new DataResponse<>(
                        "success",
                        "Product retrieved successfully",
                        "/api/product/" + id,
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        HttpStatus.OK.value(),
                        "1.0",
                        Collections.emptyList(),
                        product
                )
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StandardResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(
                StandardResponse.builder()
                        .result("success")
                        .detail("Product updated successfully")
                        .path("/api/product/" + id)
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.OK.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<StandardResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                StandardResponse.builder()
                        .result("success")
                        .detail("Product deleted successfully")
                        .path("/api/product/" + id)
                        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .code(HttpStatus.NO_CONTENT.value())
                        .version("1.0")
                        .errors(Collections.emptyList())
                        .build()
        );
    }
}