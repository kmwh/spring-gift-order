package gift.kakao.dto;

import gift.member.entity.Member;

public record KakaoLoginResponseDto(
    String token,
    Member member
) {
    public static KakaoLoginResponseDto from(String token, Member member) {
        return new KakaoLoginResponseDto(
            token,
            member
        );
    }
}
