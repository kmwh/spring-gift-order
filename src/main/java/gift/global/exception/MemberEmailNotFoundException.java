package gift.global.exception;

public class MemberEmailNotFoundException extends NotFoundException {
    public MemberEmailNotFoundException() {
        super("가입되지 않은 이메일입니다.");
    }
}
