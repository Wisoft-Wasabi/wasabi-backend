package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.domain.auth.exception.TokenNotExistException;
import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoResponse;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.web.resolver.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @SpyBean
    private MemberIdResolver memberIdResolver;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @SpyBean
    private ResponseAspect responseAspect;

    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("개인 정보 조회")
    class ReadMemberInfo {

        @DisplayName("토큰이 유효하여 개인 정보 조회에 성공한다.")
        @ParameterizedTest
        @AutoSource
        void read_member_info_success(final Member member) throws Exception {

            //given
            final Long memberId = 1L;
            final String accessToken = jwtTokenProvider.createAccessToken(memberId, "writer", Role.GENERAL, false);

            final var response = new ReadMemberInfoResponse(
                    member.getEmail(),
                    member.getName(),
                    member.getPhoneNumber(),
                    member.getRole(),
                    member.getReferenceUrl(),
                    member.getPart(),
                    member.getOrganization(),
                    member.getMotto()
            );
            given(memberService.getMemberInfo(memberId)).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    get("/members")
                            .contentType(APPLICATION_JSON)
                            .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + accessToken));

            // then
            result.andExpect(status().isOk());
        }
    }


    @Nested
    @DisplayName("회원 개인 정보 수정")
    class UpdateInfo {

        @DisplayName("요청시 정상적으로 수정사항이 반영된다.")
        @ParameterizedTest
        @AutoSource
        void update_info(final UpdateMemberInfoResponse response) throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L, "writer", Role.GENERAL, true);

            final var request = new UpdateMemberInfoRequest(
                    "name",
                    "phoneNumber",
                    "referenceUrl",
                    Part.BACKEND,
                    "organization",
                    "motto"
            );

            given(memberService.updateMemberInfo(any(), any())).willReturn(response);

            // when
            final var result = mockMvc.perform(
                    patch("/members")
                            .contentType(APPLICATION_JSON)
                            .header(Const.AUTH_HEADER,  Const.TOKEN_TYPE + " " + accessToken)
                            .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isOk());
        }

        @DisplayName("회원이 아닌 사용자가 개인 정보 수정 접근시 예외가 발생한다.")
        @ParameterizedTest
        @AutoSource
        void update_info_fail1(final TokenNotExistException exception) throws Exception {

            // given
            final var request = new UpdateMemberInfoRequest(
                    "name",
                    "phoneNumber",
                    "referenceUrl",
                    Part.BACKEND,
                    "organization",
                    "motto"
            );

            given(memberService.updateMemberInfo(any(), any())).willThrow(exception);

            // when
            final var result = mockMvc.perform(patch("/members")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isUnauthorized());
        }
    }
}