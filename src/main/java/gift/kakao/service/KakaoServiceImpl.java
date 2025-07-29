package gift.kakao.service;

import gift.kakao.dto.KakaoTokenRequestDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import gift.kakao.entity.KakaoToken;
import gift.kakao.repository.KakaoTokenRepository;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoServiceImpl implements KakaoService {
    private final KakaoTokenRepository kakaoTokenRepository;

    private final RestClient restClient;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoServiceImpl(KakaoTokenRepository kakaoTokenRepository, RestClient restClient) {
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.restClient = restClient;
    }

    @Override
    public KakaoTokenResponseDto requestAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        KakaoTokenResponseDto tokenResponse = restClient.post()
            .uri("/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(KakaoTokenResponseDto.class);

        if (tokenResponse == null) {
            throw new NullPointerException("토큰이 반환되지 않았습니다.");
        }
        return tokenResponse;
    }

    @Override
    public KakaoUserResponseDto getUserInfo(String accessToken) {
        return restClient.get()
            .uri("/v2/user/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(KakaoUserResponseDto.class);
    }

    @Override
    public URI getKakaoAuthUri() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com")
            .path("/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .build().toUri();
    }

    @Override
    public void saveToken(KakaoTokenRequestDto requestDto) {
        kakaoTokenRepository.save(KakaoToken.from(requestDto));
    }
}
