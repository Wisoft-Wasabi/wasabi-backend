package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.response.MemberLoginResponse;
import io.wisoft.wasabi.domain.auth.dto.request.MemberSignupRequest;
import io.wisoft.wasabi.domain.auth.dto.request.MemberLoginRequest;
import io.wisoft.wasabi.domain.auth.dto.response.MemberSignupResponse;
import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import io.wisoft.wasabi.global.enumeration.Role;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.bcrypt.EncryptHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EncryptHelper encryptHelper;

    @Autowired
    public AuthService(
            final MemberRepository memberRepository,
            final JwtTokenProvider jwtTokenProvider,
            final EncryptHelper encryptHelper
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.encryptHelper = encryptHelper;
    }

    @Value("${bcrypt.secret.salt}")
    private String salt;

    @Transactional
    public MemberSignupResponse signup(final MemberSignupRequest request) {
        // 1. signup DTO 중, password, checkPassword가 동일한지 check
        if (!request.password().equals(request.checkPassword())) {
            // TODO: PasswordInvalidException은 DTO Validate 과정에 문제가 발생한 것. 해당 권한으로 옮겨야함.
            throw AuthExceptionExecutor.passwordInvalid();
        }

        // 2. 중복된 회원이 있는지 조회
        if (memberRepository.existsByEmail(request.email())) {
            throw MemberExceptionExecutor.emailOverlap();
        }

        // 3. 회원가입 DTO 값을 기반으로 Member 생성
        final Member member = createMemberFromRequest(request);
        memberRepository.save(member);

        // 4. 생성한 member 기반으로 dataResponse 반환
        return new MemberSignupResponse(member);
    }

    public MemberLoginResponse login(final MemberLoginRequest request) {
        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(AuthExceptionExecutor::loginFail);

        if (!encryptHelper.isMatch(request.password(), member.getPassword())) {
            throw AuthExceptionExecutor.loginFail();
        }

        final String accessToken = jwtTokenProvider.createAccessToken(member);
        final String tokenType = "bearer";
        final String name = member.getName();
        final Role role = member.getRole();
        final boolean activation = member.isActivation();

        return new MemberLoginResponse(name, role, activation, accessToken, tokenType);
    }

    private Member createMemberFromRequest(final MemberSignupRequest request) {
        return new Member(
                request.email(),
                encryptHelper.encrypt(request.password(), salt),
                request.name(),
                request.phoneNumber(),
                false,
                Role.GENERAL,
                LocalDateTime.now().withNano(0)
        );
    }
}
