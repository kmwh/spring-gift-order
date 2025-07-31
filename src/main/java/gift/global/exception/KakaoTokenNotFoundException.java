package gift.global.exception;

public class KakaoTokenNotFoundException extends NotFoundException {
    public KakaoTokenNotFoundException() {
        super("토큰을 찾을 수 없습니다.");
    }
}
