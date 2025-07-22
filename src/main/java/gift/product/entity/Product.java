package gift.product.entity;

import gift.option.entity.Option;
import gift.product.dto.UpdateProductRequestDto;
import gift.product.vo.Name;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();

    protected Product() {}

    public Product(Long id) {
        this.id = id;
    }

    public Product(Name name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void update(UpdateProductRequestDto requestDto) {
        this.name = new Name(requestDto.name());
        this.price = requestDto.price();
        this.imageUrl = requestDto.imageUrl();
    }

    public void addOption(Option option) {
        options.add(option);
        option.setProduct(this);
    }
}
