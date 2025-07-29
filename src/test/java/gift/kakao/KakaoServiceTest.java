package gift.kakao;

import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.entity.Order;
import gift.kakao.repository.KakaoTokenRepository;
import gift.kakao.repository.OrderRepository;
import gift.kakao.service.KakaoServiceImpl;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KakaoServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionService optionService;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private KakaoTokenRepository kakaoTokenRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private KakaoServiceImpl kakaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        kakaoService = new KakaoServiceImpl(
            kakaoTokenRepository,
            optionRepository,
            orderRepository,
            optionService,
            restClient
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
    void 카카오_주문_성공_테스트() throws Exception {
        // given
        Long optionId = 1L;
        int quantity = 2;
        String message = "테스트 메시지";

        Option mockOption = mock(Option.class);
        Order mockOrder = mock(Order.class);

        // when
        when(mockOrder.getId()).thenReturn(1L);
        when(mockOption.getId()).thenReturn(optionId);
        when(mockOrder.getOption()).thenReturn(mockOption);
        when(mockOrder.getQuantity()).thenReturn(quantity);
        when(mockOrder.getOrderDateTime()).thenReturn(LocalDateTime.now());
        when(mockOrder.getMessage()).thenReturn(message);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(mockOption));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        when(httpServletRequest.getAttribute("kakaoAccessToken")).thenReturn("fake_token");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(null);

        OrderRequestDto requestDto = new OrderRequestDto(optionId, quantity, message);
        OrderResponseDto result = kakaoService.order(httpServletRequest, requestDto);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.optionId()).isEqualTo(optionId);
        assertThat(result.quantity()).isEqualTo(quantity);
        assertThat(result.message()).isEqualTo(message);

        verify(optionService).subtract(optionId, quantity);
        verify(orderRepository).save(any(Order.class));
        verify(restClient).post();
    }
}
