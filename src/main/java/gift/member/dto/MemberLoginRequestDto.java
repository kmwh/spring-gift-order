package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequestDto(
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password
) {
    public static MemberLoginRequestDto from() {
        return new MemberLoginRequestDto("", "");
    }
}
