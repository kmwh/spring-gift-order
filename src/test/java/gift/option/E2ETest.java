package gift.option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.global.dto.PageResponseDto;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.ProductResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class E2ETest {
    @LocalServerPort
    private int port;

    private String baseUrl = "";

    @Autowired
    RestClient restClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/products/1/options";
    }

    @Test
    void 옵션_목록_조회_성공_테스트() {
        // when
        var response = restClient.get()
            .uri(baseUrl)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<OptionResponseDto>>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirst().name()).isEqualTo("옵션01");
    }

    @Test
    void 단일_옵션_조회_성공_테스트() {
        // when
        var response = restClient.get()
            .uri(baseUrl + "/1")
            .retrieve()
            .toEntity(OptionResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("옵션01");
    }

    @Test
    void 존재하지_않는_단일_옵션_조회_테스트() {
        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.get()
                .uri(baseUrl + "/99")
                .retrieve()
                .toEntity(OptionResponseDto.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void 옵션_생성_성공_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("옵션 Test", 100);

        // when
        var response = restClient.post()
            .uri(baseUrl)
            .body(requestDto)
            .retrieve()
            .toEntity(OptionResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("옵션 Test");
    }

    @Test
    void 이름_15자_초과_옵션_생성_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("15자를 초과하는 옵션 생성 Test", 100);

        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .uri(baseUrl)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 사용할_수_없는_특수문자_이름_옵션_생성_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("옵션!", 100);

        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .uri(baseUrl)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 수량_1개_미만_옵션_생성_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("옵션 Test", 0);

        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .uri(baseUrl)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 수량_1억개_이상_옵션_생성_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("옵션 Test", 100000000);

        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .uri(baseUrl)
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 옵션_수정_성공_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("updated 옵션01", 100);

        // when
        var response = restClient.put()
            .uri(baseUrl + "/1")
            .body(requestDto)
            .retrieve()
            .toEntity(OptionResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("updated 옵션01");
    }

    @Test
    void 존재하지_않는_옵션_수정_테스트() {
        // given
        OptionRequestDto requestDto = new OptionRequestDto("존재하지 않는 옵션", 100);

        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.put()
                .uri(baseUrl + "/99")
                .body(requestDto)
                .retrieve()
                .toEntity(OptionResponseDto.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void 옵션_삭제_성공_테스트() {
        // when
        var response = restClient.delete()
            .uri(baseUrl + "/2")
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 존재하지_않는_옵션_삭제_테스트() {
        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.delete()
                .uri(baseUrl + "/99")
                .retrieve()
                .toEntity(Void.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void 옵션_전부_삭제_테스트() {
        // when
        var response = restClient.delete()
            .uri(baseUrl + "/1")
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // when
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.delete()
                .uri(baseUrl + "/2")
                .retrieve()
                .toEntity(Void.class));

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
