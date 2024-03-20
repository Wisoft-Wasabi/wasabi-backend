package io.wisoft.wasabi.setting;

import io.wisoft.wasabi.domain.auth.web.dto.LoginRequest;
import io.wisoft.wasabi.domain.board.web.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.comment.dto.WriteCommentRequest;
import io.wisoft.wasabi.domain.like.web.dto.RegisterLikeRequest;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.global.config.common.Const;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;

@ExtendWith(TestContainerConfig.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@Transactional(propagation = Propagation.REQUIRES_NEW)
@Import(SlackServiceTestConfig.class)
public class IntegrationTest {

    @LocalServerPort
    private long port;

    private final String serverURI = "http://localhost:";

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private EntityManager em;

    protected String adminLogin() {

        final String adminEmail = em.find(Member.class, 1L).getEmail();

        final String url = generateURL("/auth/login");
        final LoginRequest loginRequest =
            new LoginRequest(adminEmail, "!admin1234");

        final var response = client.postForEntity(url, loginRequest, Object.class);

        return getDataByKey(response, "accessToken");
    }

    protected ResponseEntity<Object> writeBoard(final String accessToken,
                                            final WriteBoardRequest request) {

        final String url = generateURL("/boards");
        final HttpHeaders authHeader = generateAuthHeader(accessToken);
        final HttpEntity<WriteBoardRequest> httpEntity = new HttpEntity<>(request, authHeader);

        return client.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

    protected ResponseEntity<Object> readBoard(final Long boardId) {

        final String url = generateURL("/boards/" + boardId);

        return client.getForEntity(url, Object.class);
    }

    protected ResponseEntity<Object> readBoardList(final int page,
                                                   final int size,
                                                   final String sortBy) {

        final String url = UriComponentsBuilder
            .fromHttpUrl(generateURL("/boards"))
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("sortBy", sortBy)
            .build()
            .toUriString();

        return client.exchange(url, HttpMethod.GET, null, Object.class);
    }

    protected ResponseEntity<Object> readMyBoards(final String accessToken) {

        final String url = UriComponentsBuilder
            .fromHttpUrl(generateURL("/boards/my-board"))
            .queryParam("page", 0)
            .queryParam("size", 3)
            .build()
            .toUriString();

        final HttpHeaders authHeader = generateAuthHeader(accessToken);
        final HttpEntity<WriteBoardRequest> httpEntity = new HttpEntity<>(authHeader);

        return client.exchange(url, HttpMethod.GET, httpEntity, Object.class);
    }

    protected ResponseEntity<Object> readMyLikeBoards(final String accessToken) {

        final String url = generateURL("/boards/my-like");

        final HttpHeaders authHeader = generateAuthHeader(accessToken);
        final HttpEntity<?> httpEntity = new HttpEntity<>(authHeader);

        return client.exchange(url, HttpMethod.GET, httpEntity, Object.class);
    }

    protected ResponseEntity<Object> registerLike(final String accessToken, final Long boardId) {

        final String url = generateURL("/likes");
        final RegisterLikeRequest request = new RegisterLikeRequest(boardId);

        final HttpHeaders authHeader = generateAuthHeader(accessToken);
        final HttpEntity<RegisterLikeRequest> httpEntity = new HttpEntity<>(request, authHeader);

        return client.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

    protected ResponseEntity<Object> writeComment(final String accessToken,
                                                  final Long boardId,
                                                  final String content) {

        final String url = generateURL("/comments");
        final WriteCommentRequest request = new WriteCommentRequest(boardId, content);

        final HttpHeaders authHeader = generateAuthHeader(accessToken);
        final HttpEntity<WriteCommentRequest> httpEntity = new HttpEntity<>(request, authHeader);

        return client.exchange(url, HttpMethod.POST, httpEntity, Object.class);
    }

    protected <T> T getDataByKey(final ResponseEntity<Object> response, final String key) {

        final LinkedHashMap<String, Object> linkedHashMap =
            (LinkedHashMap<String, Object>) response.getBody();

        return (T) ((LinkedHashMap<String, String>) linkedHashMap.get("data")).get(key);
    }

    private String generateURL(final String url) {

        return new StringBuilder()
            .append(this.serverURI)
            .append(this.port)
            .append(url)
            .toString();
    }

    private HttpHeaders generateAuthHeader(final String accessToken) {

        if (StringUtils.isNotBlank(accessToken)) {
            final String bearerToken = new StringBuilder()
                .append(Const.TOKEN_TYPE)
                .append(" ")
                .append(accessToken)
                .toString();

            final HttpHeaders authHeader = new HttpHeaders();
            authHeader.add(HttpHeaders.AUTHORIZATION, bearerToken);

            return authHeader;
        }

        return null;
    }

}
