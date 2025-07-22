package gift.member.service;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.dto.MemberResponseDto;
import java.util.List;

public interface MemberService {
    void register(MemberRegisterRequestDto requestDto);
    MemberLoginResponseDto login(MemberLoginRequestDto requestDto);
    List<MemberResponseDto> findAll();
    MemberResponseDto findById(Long id);
    void update(Long id, MemberRegisterRequestDto requestDto);
    void delete(Long id);
}
