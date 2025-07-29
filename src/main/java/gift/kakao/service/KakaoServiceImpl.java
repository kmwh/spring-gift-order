package gift.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.OptionNotFoundException;
import gift.kakao.dto.KakaoOrderRequestDto;
import gift.kakao.dto.KakaoOrderResponseDto;
import gift.kakao.dto.KakaoTokenRequestDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import gift.kakao.entity.KakaoToken;
import gift.kakao.entity.Order;
import gift.kakao.repository.KakaoTokenRepository;
import gift.kakao.repository.OrderRepository;
import gift.kakao.template.Content;
import gift.kakao.template.Link;
import gift.kakao.template.TemplateObject;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;

    private final OptionService optionService;

    @Autowired
    private RestClient restClient;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoServiceImpl(
        KakaoTokenRepository kakaoTokenRepository,
        OptionRepository optionRepository,
        OrderRepository orderRepository,
        OptionService optionService
    ) {
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
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
    public KakaoUserResponseDto getUserInfo(String accessToken) {
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
    public void saveToken(KakaoTokenRequestDto requestDto) {
        kakaoTokenRepository.save(KakaoToken.from(requestDto));
    }

    @Override
    public KakaoOrderResponseDto order(HttpServletRequest request, KakaoOrderRequestDto requestDto) {
        Optional<Option> optionOptional = optionRepository.findById(requestDto.optionId());
        Option option = optionOptional.orElseThrow(OptionNotFoundException::new);

        Order order = Order.from(
            option,
            requestDto.quantity(),
            LocalDateTime.now(),
            requestDto.message()
        );
        Order orderResponse = orderRepository.save(order);

        optionService.subtract(requestDto.optionId(), requestDto.quantity());

        KakaoOrderResponseDto responseDto = KakaoOrderResponseDto.from(orderResponse);

        // 나에게 메시지 전송
        String kakaoAccessToken = (String) request.getAttribute("kakaoAccessToken");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);

        restClient.post()
            .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(toKakaoTemplateFormData(responseDto))
            .retrieve()
            .toBodilessEntity();

        return responseDto;
    }

    public MultiValueMap<String, String> toKakaoTemplateFormData(KakaoOrderResponseDto dto) {
        // 예시로, 주문 메시지 제목, 설명, 이미지, 링크를 dto 데이터 기반으로 만듭니다.
        Link link = new Link(
            "",
            "",
            "",
            ""
        );

        Content content = new Content(
            "주문번호: " + dto.id(),
            link
        );

        TemplateObject templateObject = new TemplateObject("feed", content);

        // JSON 문자열로 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        String templateJson = null;
        try {
            templateJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // form data 생성
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("template_object", templateJson);

        return formData;
    }
}
