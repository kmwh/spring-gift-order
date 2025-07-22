package gift.wishlist.entity;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.vo.Amount;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Amount amount;

    public Wish() {}

    public Wish(Member member, Product product, Amount amount) {
        this.member = member;
        this.product = product;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Amount getAmount() {
        return amount;
    }

    public void changeAmount(Amount amount) {
        this.amount = amount;
    }
}
