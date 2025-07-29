package gift.kakao;

import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.service.KakaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private KakaoServiceImpl kakaoService;

    @Test
    void 카카오_액세스_토큰_요청_성공_테스트() {
        // given
        String code = "test-auth-code";
        KakaoTokenResponseDto mockResponse = new KakaoTokenResponseDto(
            "access-token-value",
            "bearer",
            "refresh-token-value",
            3600,
            "account_email profile",
            86400
        );

        // when
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/oauth/token")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(KakaoTokenResponseDto.class)).thenReturn(mockResponse);

        // then
        KakaoTokenResponseDto result = kakaoService.requestAccessToken(code);
        assertEquals("access-token-value", result.accessToken());
    }
}
