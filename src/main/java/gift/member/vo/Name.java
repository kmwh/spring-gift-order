package gift.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Name {
    @Column(name = "name", nullable = false)
    private String name;

    public Name() {}

    public Name(String value) {
        check(value);
        this.name = value;
    }

    private void check(String name) {
        if (name.length() < 3) {
            throw new IllegalArgumentException("이름은 3자 이상이어야 합니다.");
        }
    }

    public String getValue() {
        return name;
    }
}
