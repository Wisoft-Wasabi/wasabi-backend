package io.wisoft.wasabi.domain.admin;

import io.wisoft.wasabi.domain.member.*;
import io.wisoft.wasabi.global.config.common.annotation.AdminRoleResolver;
import io.wisoft.wasabi.global.config.common.annotation.AnyoneResolver;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.aop.UserRoleAspect;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @SpyBean
    private AdminRoleResolver adminRoleResolver;

    @MockBean
    private MemberRepository memberRepository;

    @SpyBean
    private AuthorizationExtractor authorizationExtractor;

    @SpyBean
    private UserRoleAspect userRoleAspect;

    @SpyBean
    private ResponseAspect responseAspect;


    @Nested
    @DisplayName("승인되지 않은 회원 조회")
    class getUnapprovedMembers {

        @DisplayName("관리자가 승인되지 않은 회원을 조회한다.")
        @Test
        void getUnapprovedMembers() throws Exception {

            // given
            final var member = new Member("admin@wisoft.io",
                    "alrmsl1!",
                    "MIGNI",
                    "010-1234-5678",
                    true,
                    Role.GENERAL,
                    "wisoft.io",
                    Part.BACKEND,
                    "wisoft",
                    "아자아자");

            memberRepository.save(member);
            final var token = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole(), member.isActivation());

            // when
            final var result = mockMvc.perform(get("/admin/members")
                    .header("Authorization", "bearer " + token)
                    .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isForbidden());
        }

    }
}
