package gift.kakao.dto;

import gift.member.entity.Member;

public record KakaoTokenDto(
    String accessToken,
    String refreshToken,
    int expiresIn,
    int refreshTokenExpiresIn,
    Member member
) {
    public static KakaoTokenDto from(
        String accessToken,
        String refreshToken,
        int expiresIn,
        int refreshTokenExpiresIn,
        Member member
    ) {
        return new KakaoTokenDto(
            accessToken,
            refreshToken,
            expiresIn,
            refreshTokenExpiresIn,
            member
        );
    }
}
