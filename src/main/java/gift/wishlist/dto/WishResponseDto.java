package gift.wishlist.dto;

import gift.wishlist.entity.Wish;

public record WishResponseDto(
    Long id,
    Long memberId,
    Long productId,
    Integer amount
) {
    public static WishResponseDto from(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getMember().getId(),
            wish.getProduct().getId(),
            wish.getAmount().getValue()
        );
    }
}
