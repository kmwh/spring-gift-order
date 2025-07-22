package gift.global.exception;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super("해당 상품을 찾을 수 없습니다");
    }
}
