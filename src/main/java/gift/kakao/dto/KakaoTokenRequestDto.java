package gift.kakao.dto;

import gift.member.entity.Member;

public record KakaoTokenRequestDto(
    String accessToken,
    String refreshToken,
    int expiresIn,
    int refreshTokenExpiresIn,
    Member member
) {
    public static KakaoTokenRequestDto from(
        String accessToken,
        String refreshToken,
        int expiresIn,
        int refreshTokenExpiresIn,
        Member member
    ) {
        return new KakaoTokenRequestDto(
            accessToken,
            refreshToken,
            expiresIn,
            refreshTokenExpiresIn,
            member
        );
    }
}
