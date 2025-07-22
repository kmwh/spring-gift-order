package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.global.security.JwtProvider;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
            .baseUrl("http://localhost:" + port + "/api/members")
            .build();
    }

    @Test
    void 회원가입_성공_테스트() {
        // given
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
            "test3",
            "test3@email.com",
            "testtest3"
        );

        // when
        ResponseEntity<Void> response = restClient.post()
            .uri("/register")
            .body(requestDto)
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 로그인_성공_테스트() {
        // given
        MemberLoginRequestDto requestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when
        ResponseEntity<MemberLoginResponseDto> response = restClient.post()
            .uri("/login")
            .body(requestDto)
            .retrieve()
            .toEntity(MemberLoginResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        String loginToken = response.getBody().token();
        assertThat(loginToken).isNotBlank();

        assertThat(jwtTokenProvider.validateToken(loginToken)).isTrue();
        assertThat(jwtTokenProvider.isTokenExpired(loginToken)).isFalse();
        Claims loginClaims = jwtTokenProvider.parseToken(loginToken);
        assertThat(loginClaims.getSubject()).isEqualTo("1");
        assertThat(loginClaims.get("name", String.class)).isEqualTo("test1");
    }

    @Test
    void 중복_이메일_회원가입_테스트() {
        // given
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
            "test1",
            "test1@email.com",
            "1q2w3e4r5t"
        );

        // when
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                .uri("/register")
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class);
        });

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 이메일_형식이_아닌_회원가입_테스트() {
        // given
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
            "test3",
            "testtesttest",
            "testtesttest333"
        );

        // when
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                .uri("/register")
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class);
        });

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 비밀번호_8자_미만_회원가입_테스트() {
        // given
        MemberRegisterRequestDto requestDto = new MemberRegisterRequestDto(
            "test3",
            "test3@email.com",
            "123"
        );

        // when
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                .uri("/register")
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class);
        });

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 존재하지_않는_아이디_로그인_테스트() {
        // given
        MemberLoginRequestDto requestDto = new MemberLoginRequestDto(
            "tetest11@email.com",
            "1q2w3e4r5t"
        );

        // when
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                .uri("/login")
                .body(requestDto)
                .retrieve()
                .toEntity(MemberLoginResponseDto.class);
        });

        // then
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void 잘못된_비밀번호로_로그인_테스트() {
        // given
        MemberLoginRequestDto requestDto = new MemberLoginRequestDto(
            "test1@email.com",
            "qawsedrftg"
        );

        // when & then
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                .uri("/login")
                .body(requestDto)
                .retrieve()
                .toEntity(MemberLoginResponseDto.class);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
