package gift.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProductRequestDto(
    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력 가능합니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-\\&/_\\s]+$",
        message = "상품 이름에는 (), [], +, -, &, /, _ 외의 특수 문자는 사용할 수 없습니다."
    )
    String name,

    @NotNull(message = "상품 가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    @Max(value = 99999999, message = "가격은 1억원 미만이어야 합니다.")
    Integer price,

    @NotBlank(message = "이미지 URL은 필수입니다.")
    @Size(max = 500, message = "이미지 URL은 최대 500자까지 입력 가능합니다.")
    String imageUrl
) {
    public static UpdateProductRequestDto from() {
        return new UpdateProductRequestDto(
            "",
            0,
            ""
        );
    }
}
