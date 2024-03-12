package io.wisoft.wasabi.domain.admin.web;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.admin.web.dto.*;
import io.wisoft.wasabi.domain.member.persistence.Role;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ObjectMapper objectMapper;

    @SpyBean
    private ResponseAspect responseAspect;

    private String accessToken;

    @BeforeEach
    void createToken() {
        accessToken = jwtTokenProvider.createAccessToken(
                1L,
                "admin",
                Role.ADMIN,
                true
        );
    }

    @Nested
    @DisplayName("승인되지 않은 회원 조회")
    class ReadUnapprovedMembers {

        @DisplayName("관리자가 승인되지 않은 회원을 조회한다.")
        @ParameterizedTest
        @AutoSource
        void read_unapproved_members(final List<MembersResponse> membersResponses) throws Exception {

            // given
            given(adminService.getUnapprovedMembers(any())).willReturn(new SliceImpl<>(membersResponses));

            // when
            final var result = mockMvc.perform(
                    get("/admin/members")
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("승인되지 않은 유저 승인(활성화)")
    class UpdateApproveActivateMember {

        @DisplayName("관리자가 승인되지 않은 회원을 승인한다.")
        @ParameterizedTest
        @AutoSource
        void update_approve_activate_member(final ApproveMemberRequest request) throws Exception {

            // given
            final var response = new ApproveMemberResponse(1L);

            given(adminService.approveMember(any())).willReturn(response);

            // when
            final var result = mockMvc.perform(
                    patch("/admin/members")
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("회원 가입 요청 삭제")
    class DeleteSignUp {

        @DisplayName("회원 가입 요청들을 정상적으로 삭제한다.")
        @ParameterizedTest
        @AutoSource
        void delete_sign_up_requests(final DeleteSignUpRequest request) throws Exception {

            // given
            final String content = objectMapper.writeValueAsString(request);

            given(adminService.deleteSignUp(any())).willReturn(new DeleteSignUpResponse(request.ids().size()));

            // when
            final var result = mockMvc.perform(
                    delete("/admin/members")
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .content(content)
                            .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @DisplayName("회원 가입 요청들의 id는 null 이어서는 안된다.")
        void delete_sign_up_requests_fail() throws Exception {

            // given
            final var request = new DeleteSignUpRequest(null);

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(
                    delete("/admin/members")
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken)
                            .content(content)
                            .contentType(APPLICATION_JSON));

            // then
            result.andExpect(status().isBadRequest());
        }
    }
}
