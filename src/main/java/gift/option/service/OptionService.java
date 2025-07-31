package gift.option.service;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import java.util.List;

public interface OptionService {
    OptionResponseDto create(Long productId, OptionRequestDto requestDto);
    List<OptionResponseDto> findAllByProductId(Long productId);
    OptionResponseDto findByProductIdAndOptionId(Long productId, Long optionId);
    OptionResponseDto update(Long productId, Long optionId, OptionRequestDto requestDto);
    void delete(Long productId, Long optionId);
}
