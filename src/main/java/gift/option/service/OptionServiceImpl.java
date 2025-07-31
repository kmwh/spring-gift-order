package gift.option.service;

import gift.global.exception.MinimumQuantityViolationException;
import gift.global.exception.OptionNotFoundException;
import gift.global.exception.ProductNotFoundException;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionServiceImpl implements OptionService{
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OptionResponseDto create(Long productId, OptionRequestDto requestDto) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(ProductNotFoundException::new);

        Option option = new Option(
            requestDto.name(),
            requestDto.quantity()
        );
        option.setProduct(product);

        Option optionResponse = optionRepository.save(option);
        return OptionResponseDto.from(optionResponse);
    }

    @Override
    public List<OptionResponseDto> findAllByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId)
            .stream()
            .map(OptionResponseDto::from)
            .toList();
    }

    @Override
    public OptionResponseDto findByProductIdAndOptionId(Long productId, Long optionId) {
        Optional<Option> optionOptional = optionRepository.findByIdAndProductId(optionId, productId);
        Option option = optionOptional.orElseThrow(OptionNotFoundException::new);

        return OptionResponseDto.from(option);
    }

    @Transactional
    @Override
    public OptionResponseDto update(Long productId, Long optionId, OptionRequestDto requestDto) {
        Optional<Option> optionOptional = optionRepository.findByIdAndProductId(optionId, productId);
        Option option = optionOptional.orElseThrow(OptionNotFoundException::new);

        option.updateName(requestDto.name());
        option.updateQuantity(requestDto.quantity());
        return OptionResponseDto.from(option);
    }

    @Transactional
    @Override
    public void delete(Long productId, Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new OptionNotFoundException();
        }

        if (optionRepository.findAllByProductId(productId).size() <= 1) {
            throw new MinimumQuantityViolationException();
        }

        optionRepository.deleteById(optionId);
    }
}
