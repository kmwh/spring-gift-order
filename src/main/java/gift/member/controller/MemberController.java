package gift.member.controller;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody MemberRegisterRequestDto requestDto) {
        memberService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(@Valid @RequestBody MemberLoginRequestDto requestDto) {
        return  ResponseEntity.ok(memberService.login(requestDto));
    }
}
