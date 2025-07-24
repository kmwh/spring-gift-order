package gift.member.dto;

public record MemberLoginResponseDto(
    String token
) {
    public static MemberLoginResponseDto from(String token) {
        return new MemberLoginResponseDto(
            token
        );
    }
}
