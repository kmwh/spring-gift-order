package gift.product.controller;

import gift.global.dto.PageResponseDto;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.dto.UpdateProductRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
        @Valid  @RequestBody CreateProductRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.create(requestDto));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponseDto>> findAll(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProductRequestDto requestDto
    ) {
        return ResponseEntity.ok(productService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent()
            .build();
    }
}
