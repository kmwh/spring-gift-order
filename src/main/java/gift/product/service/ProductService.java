package gift.product.service;

import gift.global.dto.PageResponseDto;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.dto.UpdateProductRequestDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDto create(CreateProductRequestDto requestDto);
    PageResponseDto<ProductResponseDto> findAll(Pageable pageable);
    ProductResponseDto findById(Long id);
    ProductResponseDto update(Long id, UpdateProductRequestDto requestDto);
    void delete(Long id);
}
