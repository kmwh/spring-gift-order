package gift.kakao.service;

import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto order(Long memberId, OrderRequestDto requestDto);
}
