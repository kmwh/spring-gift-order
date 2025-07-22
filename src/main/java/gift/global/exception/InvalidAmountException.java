package gift.global.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("수량은 비어있거나 99개를 초과할 수 없습니다.");
    }
}
