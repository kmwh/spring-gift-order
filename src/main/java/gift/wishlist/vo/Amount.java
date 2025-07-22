package gift.wishlist.vo;

import gift.global.exception.InvalidAmountException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Amount {
    @Column(nullable = false)
    private Integer amount;

    protected Amount() {}

    public Amount(int value) {
        check(value);
        this.amount = value;
    }

    private void check(Integer amount) {
        if (amount > 99) {
            throw new InvalidAmountException();
        }
    }

    public Integer getValue() {
        return amount;
    }
}
