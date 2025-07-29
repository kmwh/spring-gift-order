package gift.kakao.controller;

import gift.kakao.dto.KakaoLoginResponseDto;
import gift.kakao.dto.KakaoTokenRequestDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.dto.KakaoUserResponseDto;
import gift.kakao.service.KakaoService;
import gift.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {
    private final KakaoService kakaoService;

    private final MemberService memberService;

    public KakaoAuthController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> authRedirectToKakao() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(kakaoService.getKakaoAuthUri());

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/callback")
    public ResponseEntity<KakaoLoginResponseDto> authCallback(@RequestParam("code") String code) {
        KakaoTokenResponseDto accessToken = kakaoService.requestAccessToken(code);
        KakaoUserResponseDto userResponseDto = kakaoService.getUserInfo(accessToken.accessToken());

        KakaoLoginResponseDto loginResponseDto = memberService.loginWithKakao(userResponseDto);

        kakaoService.saveToken(KakaoTokenRequestDto.from(
            accessToken.accessToken(),
            accessToken.refreshToken(),
            accessToken.expiresIn(),
            accessToken.refreshTokenExpiresIn(),
            loginResponseDto.member()
        ));

        return ResponseEntity.ok(loginResponseDto);
    }
}
