package gift.global.exception;

public class MinimumQuantityViolationException extends RuntimeException {
    public MinimumQuantityViolationException() {
        super("옵션 수량은 최소 1개 이상입니다.");
    }
}
