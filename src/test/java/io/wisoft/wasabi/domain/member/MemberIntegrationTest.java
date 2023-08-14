package io.wisoft.wasabi.domain.member;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("개인 정보 조회")
    class ReadMemberInfo {

        @ParameterizedTest
        @AutoSource
        @DisplayName("토큰이 유효하여, 개인 정보 조회에 성공한다.")
        void read_member_info_success(final Member member) throws Exception {

            //given
            memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole());

            //when
            final var perform = mockMvc.perform(get("/members")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken));

            //then
            perform.andExpect(status().isOk());
        }
    }
}
