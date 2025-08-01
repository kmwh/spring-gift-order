package gift.cors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    RestClient restClient;

    @Test
    void cors() {
        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("http://localhost:8080");
        headers.setAccessControlRequestMethod(HttpMethod.GET);

        ResponseEntity<Void> response = restClient
            .method(HttpMethod.OPTIONS)
            .uri("http://localhost:" + port + "/api/products")
            .headers(httpHeaders -> {
                httpHeaders.addAll(headers);
            })
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders()
            .getAccessControlAllowOrigin())
            .isEqualTo("*");
        assertThat(response.getHeaders()
            .getAccessControlAllowMethods())
            .containsExactlyInAnyOrder(
                HttpMethod.GET,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE
            );
        assertThat(response
            .getHeaders()
            .getAccessControlExposeHeaders())
            .contains(HttpHeaders.LOCATION);
    }
}
