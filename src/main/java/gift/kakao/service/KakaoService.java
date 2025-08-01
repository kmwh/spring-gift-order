package gift.kakao.service;

import gift.kakao.dto.KakaoTokenDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import gift.kakao.dto.OrderResponseDto;
import java.net.URI;
import org.springframework.util.MultiValueMap;

public interface KakaoService {
    KakaoTokenResponseDto requestAccessToken(String code);
    KakaoUserResponseDto getUserId(String accessToken);
    URI getKakaoAuthUri();
    void saveToken(KakaoTokenDto requestDto);
    String getAccessTokenByMemberId(Long memberId);
    MultiValueMap<String, String> toKakaoTemplateFormDto(OrderResponseDto dto);
    void sendKakaoMessage(Long memberId, OrderResponseDto orderRequestDto);
}
