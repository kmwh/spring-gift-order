package gift.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Password {
    @Column(name = "password", nullable = false)
    private String password;

    public Password() {}

    public Password(String value) {
        check(value);
        this.password = value;
    }

    private void check(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
    }

    public String getValue() {
        return password;
    }

    public boolean matches(String inputValue) {
        return password.equals(inputValue);
    }
}
