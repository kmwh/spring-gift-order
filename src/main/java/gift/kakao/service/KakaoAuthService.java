package gift.kakao.service;

import gift.kakao.dto.KakaoTokenDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import java.net.URI;

public interface KakaoAuthService {
    KakaoTokenResponseDto requestAccessToken(String code);
    KakaoUserResponseDto getUserId(String accessToken);
    URI getKakaoAuthUri();
    void saveToken(KakaoTokenDto requestDto);
}
