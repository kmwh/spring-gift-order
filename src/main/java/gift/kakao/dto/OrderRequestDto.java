package gift.kakao.dto;

public record OrderRequestDto(
    Long optionId,
    Integer quantity,
    String message
) {
}
