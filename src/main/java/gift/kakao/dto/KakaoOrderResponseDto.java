package gift.kakao.dto;

import gift.kakao.entity.Order;
import java.time.LocalDateTime;

public record KakaoOrderResponseDto(
    Long id,
    Long optionId,
    Integer quantity,
    LocalDateTime orderDateTime,
    String message
) {
    public static KakaoOrderResponseDto from(Order order) {
        return new KakaoOrderResponseDto(
            order.getId(),
            order.getOption()
                .getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }
}
