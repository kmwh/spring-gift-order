package gift.kakao.dto;

public record KakaoOrderRequestDto(
    Long optionId,
    Integer quantity,
    String message
) {
}
