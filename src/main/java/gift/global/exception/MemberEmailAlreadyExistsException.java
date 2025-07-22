package gift.global.exception;

public class MemberEmailAlreadyExistsException extends RuntimeException {
    public MemberEmailAlreadyExistsException() {
        super("이미 존재하는 이메일 입니다.");
    }
}
