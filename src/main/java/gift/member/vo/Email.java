package gift.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email {
    @Column(name = "email", nullable = false)
    private String email;

    protected Email() {}

    public Email(String value) {
        check(value);
        this.email = value;
    }

    private void check(String email) {
        if (email.length() < 10) {
            throw new IllegalArgumentException("이메일은 10자 이상이어야 합니다.");
        }
    }

    public String getValue() {
        return email;
    }
}
