package gift.option.dto;

import gift.option.entity.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequestDto(
    @NotBlank(message = "옵션 이름은 필수입니다.")
    @Size(max = 15, message = "옵션 이름은 최대 15자까지 입력 가능합니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-\\&/_\\s]+$",
        message = "옵션 이름에는 (), [], +, -, &, /, _ 외의 특수 문자는 사용할 수 없습니다."
    )
    String name,

    @NotNull(message = "옵션의 수량은 필수입니다.")
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상입니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억개 미만입니다.")
    Integer quantity
) {
    public static OptionRequestDto from(Option option) {
        return new OptionRequestDto(
            option.getName(),
            option.getQuantity()
        );
    }
}
