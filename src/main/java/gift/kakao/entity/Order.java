package gift.kakao.entity;

import gift.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "order_date_time", nullable = false)
    private LocalDateTime orderDateTime;

    @Column(name = "message")
    private String message;

    protected Order() {}

    public Order(
        Option option,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
    ) {
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public static Order from(
        Option option,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
    ) {
        return new Order(
            option,
            quantity,
            orderDateTime,
            message
        );
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
