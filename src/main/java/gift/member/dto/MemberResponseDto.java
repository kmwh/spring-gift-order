package gift.member.dto;

import gift.member.entity.Member;

public record MemberResponseDto(
    Long id,
    String name,
    String email,
    String password
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
            member.getId(),
            member.getName()
                .getValue(),
            member.getEmail()
                .getValue(),
            member.getPassword()
                .getValue()
        );
    }
}
