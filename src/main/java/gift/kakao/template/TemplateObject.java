package gift.kakao.template;

import gift.kakao.dto.OrderResponseDto;

public record TemplateObject(
    String object_type,
    Content content
) {
    public static TemplateObject from(OrderResponseDto dto) {
        Link link = new Link(
            "",
            "",
            "",
            ""
        );
        Content content = new Content("주문번호: " + dto.id(), link);

        return new TemplateObject("feed", content);
    }
}
