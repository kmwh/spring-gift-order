package gift.kakao.controller;

import gift.kakao.dto.KakaoUserResponseDto;
import gift.kakao.service.KakaoService;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.service.MemberService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
public class KakaoController {
    private final KakaoService kakaoService;

    private final MemberService memberService;

    public KakaoController(KakaoService kakaoService, MemberService memberService) {
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
    public ResponseEntity<MemberLoginResponseDto> authCallback(@RequestParam("code") String code) {
        String accessToken = kakaoService.requestAccessToken(code);
        KakaoUserResponseDto userResponseDto = kakaoService.getUserInfo(accessToken);

        return ResponseEntity.ok(memberService.loginWithKakao(userResponseDto));
    }
}
