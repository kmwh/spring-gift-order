package gift.global.exception;

public class ProductNameContainsKakaoException extends RuntimeException {
    public ProductNameContainsKakaoException() {
        super("'카카오'가 포함된 문구는 담당 MD와 협의 후 사용할 수 있습니다.");
    }
}
