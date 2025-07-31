package gift.kakao.service;

import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import org.springframework.util.MultiValueMap;

public interface OrderService {
    OrderResponseDto order(Long memberId, OrderRequestDto requestDto);
    String getAccessTokenByMemberId(Long memberId);
    MultiValueMap<String, String> toKakaoTemplateFormDto(OrderResponseDto dto);
}
