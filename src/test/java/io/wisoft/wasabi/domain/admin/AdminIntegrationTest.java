package io.wisoft.wasabi.domain.admin;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.domain.admin.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.DeleteSignUpRequest;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + token)
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
                .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + token)
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

    @Nested
    @DisplayName("회원가입 요청 삭제")
    class DeleteSignUp {

        private final String token = jwtTokenProvider.createAccessToken(1L, "admin", Role.ADMIN, true);

        @DisplayName("회원 가입 요청들을 성공적으로 삭제한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void delete_sign_up_requests(final List<Member> members) throws Exception {

            // given
            memberRepository.saveAll(members);
            final List<Long> ids =
                members.stream()
                    .map(Member::getId)
                    .toList();

            final DeleteSignUpRequest request = new DeleteSignUpRequest(ids);

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(delete("/admin/members")
                .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + token)
                .contentType(APPLICATION_JSON)
                .content(content));

            // then
            result.andExpect(status().isOk());
        }

        @DisplayName("회원 가입 요청들의 id는 null 이어서는 안된다.")
        @Test
        void delete_sign_up_requests_fail1() throws Exception {

            // given
            final DeleteSignUpRequest request = new DeleteSignUpRequest(null);

            final String content = objectMapper.writeValueAsString(request);

            // when
            final var result = mockMvc.perform(delete("/admin/members")
                .header(Const.AUTH_HEADER, Const.TOKEN_TYPE + " " + token)
                .contentType(APPLICATION_JSON)
                .content(content));

            // then
            result.andExpect(status().isBadRequest());
        }
    }
}
