package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.global.dto.PageResponseDto;
import gift.global.security.JwtProvider;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.wishlist.dto.CreateWishRequestDto;
import gift.wishlist.dto.UpdateWishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class E2ETest {
    @LocalServerPort
    private int port;

    RestClient restClient;

    @Autowired
    JwtProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port + "/api")
            .build();
    }

    @Test
    void 위시_리스트_생성_성공_테스트() {
        // given (로그인)
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when (로그인)
        ResponseEntity<MemberLoginResponseDto> loginResponse = restClient.post()
            .uri("/members/login")
            .body(loginRequestDto)
            .retrieve()
            .toEntity(MemberLoginResponseDto.class);

        // then (로그인)
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        String loginToken = loginResponse.getBody().token();
        assertThat(loginToken).isNotBlank();
        System.out.println(loginToken);

        // given (위시 리스트)
        CreateWishRequestDto requestDto = new CreateWishRequestDto(1L, 10);

        // when (위시 리스트)
        ResponseEntity<Void> response = restClient.post()
            .uri("/wishes")
            .header("Authorization", "Bearer " + loginToken)
            .body(requestDto)
            .retrieve()
            .toEntity(Void.class);

        // then (위시 리스트)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 위시_리스트_조회_성공_테스트() {
        // given (로그인)
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when (로그인)
        ResponseEntity<MemberLoginResponseDto> loginResponse = restClient.post()
            .uri("/members/login")
            .body(loginRequestDto)
            .retrieve()
            .toEntity(MemberLoginResponseDto.class);

        // then (로그인)
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        String loginToken = loginResponse.getBody().token();
        assertThat(loginToken).isNotBlank();
        System.out.println(loginToken);

        // when (위시 리스트)
        ResponseEntity<PageResponseDto<WishResponseDto>> response = restClient.get()
            .uri("/wishes?page=0&size=1")
            .header("Authorization", "Bearer " + loginToken)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<PageResponseDto<WishResponseDto>>() {
            });

        // then (위시 리스트)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().content().getFirst().memberId()).isEqualTo(1);
        assertThat(response.getBody().content().size()).isEqualTo(1);

        // when (위시 리스트)
        response = restClient.get()
            .uri("/wishes?page=1&size=1")
            .header("Authorization", "Bearer " + loginToken)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<PageResponseDto<WishResponseDto>>() {
            });

        // then (위시 리스트)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().content().getFirst().productId()).isEqualTo(2);
        assertThat(response.getBody().content().size()).isEqualTo(1);
    }

    @Test
    void 위시_리스트_수정_성공_테스트() {
        // given (로그인)
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when (로그인)
        ResponseEntity<MemberLoginResponseDto> loginResponse = restClient.post()
            .uri("/members/login")
            .body(loginRequestDto)
            .retrieve()
            .toEntity(MemberLoginResponseDto.class);

        // then (로그인)
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        String loginToken = loginResponse.getBody().token();
        assertThat(loginToken).isNotBlank();
        System.out.println(loginToken);

        // given (위시 리스트)
        UpdateWishRequestDto requestDto = new UpdateWishRequestDto(10);

        // when (위시 리스트)
        ResponseEntity<Void> response = restClient.put()
            .uri("/wishes/1")
            .header("Authorization", "Bearer " + loginToken)
            .body(requestDto)
            .retrieve()
            .toEntity(Void.class);

        // then (위시 리스트)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 수량_초과_수정_테스트() {
        // given (로그인)
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when (로그인)
        ResponseEntity<MemberLoginResponseDto> loginResponse = restClient.post()
            .uri("/members/login")
            .body(loginRequestDto)
            .retrieve()
            .toEntity(MemberLoginResponseDto.class);

        // then (로그인)
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        String loginToken = loginResponse.getBody().token();
        assertThat(loginToken).isNotBlank();
        System.out.println(loginToken);

        // given (위시 리스트)
        UpdateWishRequestDto requestDto = new UpdateWishRequestDto(100);

        // when (위시 리스트)
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.put()
                .uri("/wishes/1")
                .header("Authorization", "Bearer " + loginToken)
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class));

        // then (위시 리스트)
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 위시_리스트_삭제_성공_테스트() {
        // given (로그인)
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when (로그인)
        ResponseEntity<MemberLoginResponseDto> loginResponse = restClient.post()
            .uri("/members/login")
            .body(loginRequestDto)
            .retrieve()
            .toEntity(MemberLoginResponseDto.class);

        // then (로그인)
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        String loginToken = loginResponse.getBody().token();
        assertThat(loginToken).isNotBlank();
        System.out.println(loginToken);

        // when (위시 리스트)
        ResponseEntity<Void> response = restClient.delete()
            .uri("/wishes/1")
            .header("Authorization", "Bearer " + loginToken)
            .retrieve()
            .toEntity(Void.class);

        // then (위시 리스트)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}

