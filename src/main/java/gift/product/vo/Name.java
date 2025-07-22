package gift.product.vo;

import gift.global.exception.ProductNameContainsKakaoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Name {
    @Column(nullable = false)
    private String name;

    public Name() {}

    public Name(String value) {
        check(value);
        this.name = value;
    }

    private void check(String value) {
        if (value.contains("카카오")) {
            throw new ProductNameContainsKakaoException();
        }
        if (!value.matches("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-\\&/_\\s]+$")) {
            throw new IllegalArgumentException("상품 이름에는 (), [], +, -, &, /, _ 외의 특수 문자는 사용할 수 없습니다.");
        }
    }

    public String getValue() {
        return name;
    }
}
