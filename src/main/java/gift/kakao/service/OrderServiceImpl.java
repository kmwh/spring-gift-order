package gift.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.KakaoTokenNotFoundException;
import gift.global.exception.OptionNotFoundException;
import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.entity.KakaoToken;
import gift.kakao.entity.Order;
import gift.kakao.repository.KakaoTokenRepository;
import gift.kakao.repository.OrderRepository;
import gift.kakao.template.TemplateObject;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderServiceImpl implements OrderService{
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final KakaoTokenRepository kakaoTokenRepository;

    private final RestClient restClient;

    public OrderServiceImpl(
        OptionRepository optionRepository,
        OrderRepository orderRepository,
        KakaoTokenRepository kakaoTokenRepository,
        RestClient restClient
    ) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.restClient = restClient;
    }

    @Transactional
    @Override
    public OrderResponseDto order(Long memberId, OrderRequestDto requestDto) {
        Optional<Option> optionOptional = optionRepository.findById(requestDto.optionId());
        Option option = optionOptional.orElseThrow(OptionNotFoundException::new);

        Order order = Order.from(
            option,
            requestDto.quantity(),
            LocalDateTime.now(),
            requestDto.message()
        );
        Order orderResponse = orderRepository.save(order);
        OrderResponseDto responseDto = OrderResponseDto.from(orderResponse);
        option.subtract(requestDto.quantity());

        // 나에게 메시지 전송
        String kakaoAccessToken = getAccessTokenByMemberId(memberId);

        restClient.post()
            .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(toKakaoTemplateFormDto(responseDto))
            .retrieve()
            .toBodilessEntity();

        return responseDto;
    }

    public String getAccessTokenByMemberId(Long memberId) {
        Optional<KakaoToken> kakaoTokenOptional = kakaoTokenRepository.findByMemberId(memberId);
        KakaoToken kakaoToken = kakaoTokenOptional.orElseThrow(KakaoTokenNotFoundException::new);

        return kakaoToken.getAccessToken();
    }

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
