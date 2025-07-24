package gift.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserResponseDto(
    @JsonProperty("id") Long id
) {
}

