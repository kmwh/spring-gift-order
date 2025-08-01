package gift.kakao;

import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.entity.KakaoToken;
import gift.kakao.entity.Order;
import gift.kakao.repository.KakaoTokenRepository;
import gift.kakao.repository.OrderRepository;
import gift.kakao.service.KakaoServiceImpl;
import gift.kakao.service.OrderServiceImpl;
import gift.member.entity.Member;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KakaoTokenRepository kakaoTokenRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @InjectMocks
    private KakaoServiceImpl kakaoService;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        kakaoService = new KakaoServiceImpl(
            kakaoTokenRepository,
            restClient
        );

        orderService = new OrderServiceImpl(
            optionRepository,
            orderRepository,
            kakaoService
        );
    }

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
        when(requestBodyUriSpec.uri("https://kauth.kakao.com/oauth/token")).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(KakaoTokenResponseDto.class)).thenReturn(mockResponse);

        // then
        KakaoTokenResponseDto result = kakaoService.requestAccessToken(code);
        assertEquals("access-token-value", result.accessToken());
    }

    @Test
    void 카카오_주문_성공_테스트() {
        // given
        Long memberId = 1L;
        Long optionId = 100L;
        int quantity = 2;
        String message = "테스트";
        String kakaoAccessToken = "mockAccessToken";

        OrderRequestDto requestDto = new OrderRequestDto(
            optionId,
            quantity,
            message
        );

        Option option = mock(Option.class);
        when(option.getId()).thenReturn(optionId);

        Order savedOrder = Order.from(
            option,
            quantity,
            LocalDateTime.now(),
            message
        );

        KakaoToken token = new KakaoToken(
            kakaoAccessToken,
            "refresh",
            3600,
            86400,
            mock(Member.class)
        );

        // mocking
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(kakaoTokenRepository.findByMemberId(memberId)).thenReturn(Optional.of(token));

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

        // when
        OrderResponseDto result = orderService.order(memberId, requestDto);

        // then
        assertNotNull(result);
        verify(optionRepository).findById(optionId);
        verify(orderRepository).save(any(Order.class));
        verify(restClient).post();
    }
}
