package gift.product.dto;

import gift.option.dto.OptionResponseDto;
import gift.product.entity.Product;
import java.util.List;

public record ProductResponseDto(
    Long id,
    String name,
    Integer price,
    String imageUrl,
    List<OptionResponseDto> options
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName()
                .getValue(),
            product.getPrice(),
            product.getImageUrl(),
            product.getOptions()
                .stream()
                .map(OptionResponseDto::from)
                .toList()
        );
    }
}
