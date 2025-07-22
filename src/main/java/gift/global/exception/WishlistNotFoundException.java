package gift.global.exception;

public class WishlistNotFoundException extends NotFoundException {
    public WishlistNotFoundException() {
        super("해당 상품을 찾을 수 없습니다.");
    }
}
