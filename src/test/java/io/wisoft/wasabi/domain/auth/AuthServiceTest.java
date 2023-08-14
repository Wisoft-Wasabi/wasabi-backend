package io.wisoft.wasabi.domain.auth;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.auth.dto.request.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.domain.auth.exception.LoginFailException;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberMapper;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.domain.member.exception.EmailOverlapException;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EncryptHelper encryptHelper;

    @Mock
    private MemberMapper memberMapper;

    @Nested
    @DisplayName("회원 가입")
    class SignUp {

        @ParameterizedTest
        @AutoSource
        @DisplayName("회원가입이 정상적으로 동작하여, 회원이 생성된다.")
        void signUp_success(final SignupRequest request) throws Exception {

            //given
            given(encryptHelper.encrypt(any(), any())).willReturn(request.password());

            final Member mockMember = new Member(
                    request.email(),
                    encryptHelper.encrypt(request.password(), "test"),
                    request.name(),
                    request.phoneNumber(),
                    false,
                    Role.GENERAL,
                    request.referenceUrl(),
                    request.part(),
                    request.organization(),
                    request.motto()
            );
            given(memberMapper.signUpRequestToEntity(request)).willReturn(mockMember);

            final Member member = memberMapper.signUpRequestToEntity(request);
            given(memberRepository.existsByEmail(request.email())).willReturn(false);

            final SignupResponse mockResponse = new SignupResponse(
                    1L,
                    request.name()
            );
            given(memberMapper.entityToMemberSignupResponse(any())).willReturn(mockResponse);

            //when
            final var response = authService.signup(request);

            //then
            assertThat(response.name()).isEqualTo(member.getName());
        }


        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일이 중복되어, 회원가입에 실패한다.")
        void signUp_fail_duplicate_email(final SignupRequest request) {

            //given
            given(encryptHelper.encrypt(any(), any())).willReturn(request.password());

            final Member mockMember = new Member(
                    request.email(),
                    encryptHelper.encrypt(request.password(), "test"),
                    request.name(),
                    request.phoneNumber(),
                    false,
                    Role.GENERAL,
                    request.referenceUrl(),
                    request.part(),
                    request.organization(),
                    request.motto()
            );

            given(memberMapper.signUpRequestToEntity(request)).willReturn(mockMember);
            memberMapper.signUpRequestToEntity(request);

            given(memberRepository.existsByEmail(request.email())).willReturn(true);

            //when

            //then
            assertThrows(EmailOverlapException.class, () -> {
                authService.signup(request);
            });
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일, 비밀번호가 일치하여 로그인에 성공한다.")
        void login_success(final Member member) throws Exception {

            //given
            final String TOKEN_TYPE = "bearer";
            final String ACCESS_TOKEN = "accessToken";
            final var request = new LoginRequest(member.getEmail(), member.getPassword());

            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));
            given(encryptHelper.isMatch(any(), any())).willReturn(true);
            given(jwtTokenProvider.createAccessToken(any(), any(), any())).willReturn(ACCESS_TOKEN);

            final var mockResponse = new LoginResponse(
                    member.getName(),
                    member.getRole(),
                    member.isActivation(),
                    ACCESS_TOKEN,
                    TOKEN_TYPE
            );

            given(memberMapper.mapToLoginResponse(member, ACCESS_TOKEN)).willReturn(mockResponse);

            //when
            final var response = authService.login(request);

            //then
            assertThat(response.accessToken()).isEqualTo(ACCESS_TOKEN);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("존재하지 않는 이메일이 입력으로 들어와 로그인에 실패한다.")
        void login_fail_not_exist_email(final LoginRequest request) throws Exception {

            //given
            given(memberRepository.findMemberByEmail(request.email())).willThrow(LoginFailException.class);

            //when

            //then
            Assertions.assertThrows(LoginFailException.class, () -> {
                authService.login(request);
            });
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일은 존재하지만, 비밀번호가 존재하지 않아 로그인에 실패한다.")
        void login_fail_invalid_password(final Member member) throws Exception {

            //given
            final var request = new LoginRequest(member.getEmail(), member.getPassword());
            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));
            given(encryptHelper.isMatch(any(), any())).willReturn(false);

            //when

            //then
            Assertions.assertThrows(LoginFailException.class, () -> {
                authService.login(request);
            });
        }
    }
}