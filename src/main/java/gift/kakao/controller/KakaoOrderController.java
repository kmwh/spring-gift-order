package gift.kakao.controller;

import gift.kakao.dto.KakaoOrderRequestDto;
import gift.kakao.dto.KakaoOrderResponseDto;
import gift.kakao.service.KakaoService;
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
    private final KakaoService kakaoService;

    public KakaoOrderController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @PostMapping
    public ResponseEntity<KakaoOrderResponseDto> order(
        HttpServletRequest request,
        @RequestBody KakaoOrderRequestDto requestDto
    ) {
        KakaoOrderResponseDto responseDto = kakaoService.order(request, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(responseDto);
    }
}
