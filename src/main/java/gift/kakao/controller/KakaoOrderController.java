package gift.kakao.controller;

import gift.kakao.dto.OrderRequestDto;
import gift.kakao.dto.OrderResponseDto;
import gift.kakao.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class KakaoOrderController {
    private final OrderService orderService;

    public KakaoOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> order(
        HttpServletRequest request,
        @RequestBody OrderRequestDto requestDto
    ) {
        Long memberId = (Long) request.getAttribute("memberId");

        OrderResponseDto responseDto = orderService.order(memberId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(responseDto);
    }
}
