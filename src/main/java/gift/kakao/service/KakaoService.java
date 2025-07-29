package gift.kakao.service;

import gift.kakao.dto.KakaoOrderRequestDto;
import gift.kakao.dto.KakaoOrderResponseDto;
import gift.kakao.dto.KakaoTokenRequestDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;

public interface KakaoService {
    KakaoTokenResponseDto requestAccessToken(String code);
    KakaoUserResponseDto getUserInfo(String accessToken);
    URI getKakaoAuthUri();
    void saveToken(KakaoTokenRequestDto requestDto);
    KakaoOrderResponseDto order(HttpServletRequest request, KakaoOrderRequestDto requestDto);
}
