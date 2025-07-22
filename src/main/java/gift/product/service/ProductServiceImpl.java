package gift.product.service;

import gift.global.dto.PageResponseDto;
import gift.global.exception.ProductNotFoundException;
import gift.option.dto.OptionRequestDto;
import gift.option.entity.Option;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.dto.UpdateProductRequestDto;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.product.vo.Name;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDto create(CreateProductRequestDto requestDto) {
        Product product = new Product(
            new Name(requestDto.name()),
            requestDto.price(),
            requestDto.imageUrl()
        );

        for (OptionRequestDto optionResponseDto : requestDto.options()) {
            Option option = new Option(optionResponseDto.name(), optionResponseDto.quantity());
            product.addOption(option);
        }
        Product productResponse = productRepository.save(product);

        return ProductResponseDto.from(productResponse);
    }

    @Override
    public PageResponseDto<ProductResponseDto> findAll(Pageable pageable) {
        Page<ProductResponseDto> productResponseDtoPage =
            productRepository.findAll(pageable)
                .map(ProductResponseDto::from);
        return PageResponseDto.from(productResponseDtoPage);
    }

    @Override
    public ProductResponseDto findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(ProductNotFoundException::new);

        return ProductResponseDto.from(product);
    }

    @Transactional
    @Override
    public ProductResponseDto update(Long id, UpdateProductRequestDto requestDto) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(ProductNotFoundException::new);

        product.update(requestDto);
        return ProductResponseDto.from(product);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        }

        productRepository.deleteById(id);
    }
}
