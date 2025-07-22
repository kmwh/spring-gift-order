package gift.global.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDto<T>(
    List<T> content,
    int number,
    int size,
    int totalPages,
    long totalElements,
    boolean first,
    boolean last,
    boolean empty
) {
    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.isFirst(),
            page.isLast(),
            page.isEmpty()
        );
    }
}
