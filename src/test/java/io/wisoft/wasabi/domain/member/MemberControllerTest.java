package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.jwt.AuthorizationExtractor;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthorizationExtractor extractor;

    @Nested
    @DisplayName("개인 정보 조회")
    class ReadMemberInfo {

        @AutoSource
        @ParameterizedTest
        @DisplayName("토큰이 유효하여 개인 정보 조회에 성공한다.")
        void read_member_info_success(final Member member) throws Exception {

            //given
            final Long memberId = 1L;
            final String accessToken = jwtTokenProvider.createAccessToken(memberId, "writer", Role.GENERAL);

            final var mockResponse = new ReadMemberInfoResponse(
                    member.getEmail(),
                    member.getName(),
                    member.getPhoneNumber(),
                    member.getRole(),
                    member.getReferenceUrl(),
                    member.getPart(),
                    member.getOrganization(),
                    member.getMotto()
            );
            given(memberService.getMemberInfo(memberId)).willReturn(mockResponse);

            //when
            final var perform = mockMvc.perform(get("/members")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken));

            // then
            perform.andExpect(status().isOk());
        }
    }
}