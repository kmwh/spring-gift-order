package gift.kakao.service;

import gift.kakao.dto.KakaoUserResponseDto;
import java.net.URI;

public interface KakaoService {
    String requestAccessToken(String code);
    KakaoUserResponseDto getUserInfo(String accessToken);
    URI getKakaoAuthUri();
}
