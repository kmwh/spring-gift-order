package gift.kakao.service;

import gift.global.exception.OptionNotFoundException;
import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.entity.Order;
import gift.kakao.repository.OrderRepository;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService{
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;

    private final KakaoService kakaoService;

    public OrderServiceImpl(
        OptionRepository optionRepository,
        OrderRepository orderRepository,
        KakaoService kakaoService
    ) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    @Override
    public OrderResponseDto order(Long memberId, OrderRequestDto orderRequestDto) {
        Optional<Option> optionOptional = optionRepository.findById(orderRequestDto.optionId());
        Option option = optionOptional.orElseThrow(OptionNotFoundException::new);

        Order order = Order.from(
            option,
            orderRequestDto.quantity(),
            LocalDateTime.now(),
            orderRequestDto.message()
        );
        Order orderResponse = orderRepository.save(order);
        OrderResponseDto orderResponseDto = OrderResponseDto.from(orderResponse);
        option.subtract(orderRequestDto.quantity());

        // 나에게 메시지 전송
        kakaoService.sendKakaoMessage(memberId, orderResponseDto);

        return orderResponseDto;
    }
}
