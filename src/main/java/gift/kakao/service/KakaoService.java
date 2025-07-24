package gift.kakao.service;

import gift.kakao.dto.KakaoUserResponseDto;

public interface KakaoService {
    String requestAccessToken(String code);
    KakaoUserResponseDto getUserInfo(String accessToken);
}
