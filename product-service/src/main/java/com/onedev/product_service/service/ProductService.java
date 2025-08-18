package com.onedev.product_service.service;

import com.onedev.product_service.dto.ProductRequest;
import com.onedev.product_service.dto.ProductResponse;
import com.onedev.product_service.entity.Product;
import com.onedev.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import com.onedev.product_service.dto.response.DatatableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();

        productRepository.save(product);
    }

    public DatatableResponse<ProductResponse> getAllProducts(int page, int limit, String sortField, String sortOrder) {
        try {
            String[] allowedOrder = {"id", "name", "price", "quantity"}; // Define allowed sort fields
            Sort sort = Sort.by(Sort.Direction.ASC, "id"); // Default sort

            if (Arrays.asList(allowedOrder).contains(sortField)) {
                sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(Sort.Direction.ASC, sortField) : Sort.by(Sort.Direction.DESC, sortField);
            }

            Pageable pageable = PageRequest.of(page - 1, limit, sort);
            Page<Product> pageResult = productRepository.findAll(pageable);

            return toDatatableResponse(pageResult, page, limit, this::mapToProductResponse);
        } catch (Exception e) {
            // Log the error
            throw new RuntimeException("Error while getting paginated products: " + e.getMessage(), e);
        }
    }

    private DatatableResponse<ProductResponse> toDatatableResponse(Page<Product> pageResult, int page, int limit, Function<Product, ProductResponse> mapper) {
        List<ProductResponse> content = pageResult.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return DatatableResponse.<ProductResponse>builder()
                .data(content)
                .recordsTotal(pageResult.getTotalElements())
                .recordsFiltered(pageResult.getTotalElements()) // For simple cases, recordsFiltered can be same as recordsTotal
                .page(page)
                .limit(limit)
                .build();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToProductResponse(product);
    }

    public void updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}