package gift.wishlist.dto;

public record CreateWishRequestDto(
    Long productId,
    int amount
) {
}
