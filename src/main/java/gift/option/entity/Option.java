package gift.option.entity;

import gift.global.exception.MinimumQuantityViolationException;
import gift.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "option",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "name"})
    }
)
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {}

    public Option(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateQuantity(Integer quantity) {
        if (quantity < 1) {
            throw new MinimumQuantityViolationException();
        }
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void subtract(int quantity) {
        updateQuantity(getQuantity() - quantity);
    }
}
