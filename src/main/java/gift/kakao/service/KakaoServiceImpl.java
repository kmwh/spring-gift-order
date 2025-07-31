package gift.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.KakaoTokenNotFoundException;
import gift.kakao.dto.KakaoTokenDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.entity.KakaoToken;
import gift.kakao.repository.KakaoTokenRepository;
import gift.kakao.template.TemplateObject;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    public KakaoServiceImpl(
        KakaoTokenRepository kakaoTokenRepository,
        RestClient restClient
    ) {
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
            .uri("https://kauth.kakao.com/oauth/token")
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
    public KakaoUserResponseDto getUserId(String accessToken) {
        return restClient.get()
            .uri("https://kapi.kakao.com/v1/user/access_token_info")
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
    public void saveToken(KakaoTokenDto kakaoTokenDto) {
        kakaoTokenRepository.save(KakaoToken.from(kakaoTokenDto));
    }

    @Override
    public void sendKakaoMessage(Long memberId, OrderResponseDto orderResponseDto) {
        String kakaoAccessToken = getAccessTokenByMemberId(memberId);

        restClient.post()
            .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(toKakaoTemplateFormDto(orderResponseDto))
            .retrieve()
            .toBodilessEntity();
    }

    @Override
    public String getAccessTokenByMemberId(Long memberId) {
        Optional<KakaoToken> kakaoTokenOptional = kakaoTokenRepository.findByMemberId(memberId);
        KakaoToken kakaoToken = kakaoTokenOptional.orElseThrow(KakaoTokenNotFoundException::new);

        return kakaoToken.getAccessToken();
    }

    @Override
    public MultiValueMap<String, String> toKakaoTemplateFormDto(OrderResponseDto dto) {
        TemplateObject templateObject = TemplateObject.from(dto);

        // JSON 문자열로 직렬화
        String templateJson;
        try {
            templateJson = new ObjectMapper().writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // form data 생성
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("template_object", templateJson);

        return formData;
    }
}
