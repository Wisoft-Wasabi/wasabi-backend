package io.wisoft.wasabi.domain.admin;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.admin.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EncryptHelper encryptHelper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Nested
    @DisplayName("관리자 권한")
    class Admin {
        final String token = jwtTokenProvider.createAccessToken(1L, "admin", Role.ADMIN, true);

        @DisplayName("관리자가 승인되지 않은 회원을 조회한다.")
        @AutoSource
        @ParameterizedTest
        @Customization(NotSaveMemberCustomization.class)
        void getUnApproveMember(final Member member) throws Exception {

            //given
            memberRepository.save(member);

            //when
            final var result = mockMvc.perform(get("/admin/members")
                    .header("Authorization", "bearer " + token)
                    .contentType(APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content[0].name").value(member.getName()));
        }

        @DisplayName("관리자가 승인되지 않은 회원을 승인한다.")
        @AutoSource
        @ParameterizedTest
        @Customization(NotSaveMemberCustomization.class)
        void approveMember(final Member member) throws Exception {

            // given
            memberRepository.save(member);

            final ApproveMemberRequest request = new ApproveMemberRequest(member.getId());

            //when
            final var result = mockMvc.perform(patch("/admin/members")
                    .header("Authorization", "bearer " + token)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON));

            final Member approvedMember = memberRepository.findById(member.getId())
                    .orElse(null);

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(member.getId()));
            assertTrue(approvedMember.isActivation());
        }

    }
}
