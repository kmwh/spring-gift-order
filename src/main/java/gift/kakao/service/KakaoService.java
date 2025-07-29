package gift.kakao.service;

import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.dto.KakaoTokenRequestDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import java.net.URI;

public interface KakaoService {
    KakaoTokenResponseDto requestAccessToken(String code);
    KakaoUserResponseDto getUserInfo(String accessToken);
    URI getKakaoAuthUri();
    void saveToken(KakaoTokenRequestDto requestDto);
    OrderResponseDto order(Long memberId, OrderRequestDto requestDto);
}
