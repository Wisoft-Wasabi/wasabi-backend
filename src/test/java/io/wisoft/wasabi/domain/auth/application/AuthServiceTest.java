package io.wisoft.wasabi.domain.auth.application;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.auth.exception.LoginFailException;
import io.wisoft.wasabi.domain.auth.web.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.web.dto.SignupRequest;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.EmailOverlapException;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.global.config.common.bcrypt.BcryptEncoder;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    static void setup() {
        //
        new BcryptEncoder("$2a$10$wisoftwasabiprojectsaltkey02023");
//        new BcryptEncoder("$2a$10$wisoftwasabiprojectsaltkey02023");
    }

    @Nested
    @DisplayName("회원 가입")
    class SignUp {

        @ParameterizedTest
        @AutoSource
        @DisplayName("회원가입이 정상적으로 동작하여, 회원이 생성된다.")
        void signUp_success(final SignupRequest request) {

            //given

            given(memberRepository.existsByEmail(request.email())).willReturn(false);

            //when
            final var response = authService.signup(request);

            //then
                assertThat(response).isNotNull();
        }


        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일이 중복되어, 회원가입에 실패한다.")
        void signUp_fail_duplicate_email(final SignupRequest request) {

            //given
            given(memberRepository.existsByEmail(request.email())).willReturn(true);

            //when

            //then
            assertThrows(EmailOverlapException.class, () -> authService.signup(request));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일, 비밀번호가 일치하여 로그인에 성공한다.")
        void login_success(final Member member) {

            //given
            final var ACCESS_TOKEN = "accessToken";
            final var mockMember = new Member(
                member.getEmail(),
                BcryptEncoder.encrypt(member.getPassword()),
                member.getName(),
                member.getPhoneNumber(),
                false,
                member.getRole(),
                member.getReferenceUrl(),
                member.getPart(),
                member.getOrganization(),
                member.getMotto()
            );
            final var request = new LoginRequest(mockMember.getEmail(), member.getPassword());

            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(mockMember));
            given(jwtTokenProvider.createAccessToken(eq(mockMember.getId()), eq(mockMember.getName()), eq(mockMember.getRole()), anyBoolean())).willReturn(ACCESS_TOKEN);

            //when
            final var response = authService.login(request);

            //then
            assertThat(response.accessToken()).isEqualTo(ACCESS_TOKEN);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("존재하지 않는 이메일이 입력으로 들어와 로그인에 실패한다.")
        void login_fail_not_exist_email(final LoginRequest request) {

            //given
            given(memberRepository.findMemberByEmail(request.email())).willThrow(LoginFailException.class);

            //when

            //then
            assertThrows(LoginFailException.class, () -> authService.login(request));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일은 존재하지만, 비밀번호가 존재하지 않아 로그인에 실패한다.")
        void login_fail_invalid_password(final Member member) {

            //given
            final var request = new LoginRequest(member.getEmail(), member.getPassword());
            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));

            //when

            //then
            assertThrows(LoginFailException.class, () -> authService.login(request));
        }
    }
}