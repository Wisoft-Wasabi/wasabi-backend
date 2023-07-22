package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.request.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.MemberLoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.MemberSignupResponse;
import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberMapper;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.global.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.enumeration.Role;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EncryptHelper encryptHelper;
    private final MemberMapper memberMapper;

    @Autowired
    public AuthService(
            final MemberRepository memberRepository,
            final JwtTokenProvider jwtTokenProvider,
            final EncryptHelper encryptHelper,
            final MemberMapper memberMapper
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.encryptHelper = encryptHelper;
        this.memberMapper = memberMapper;
    }

    @Value("${bcrypt.secret.salt}")
    private String salt;

    @Transactional
    public MemberSignupResponse signup(final SignupRequest request) {
        // 1. signup DTO 중, password, checkPassword가 동일한지 check
        if (!request.password().equals(request.checkPassword())) {
            // TODO: PasswordInvalidException은 DTO Validate 과정에 문제가 발생한 것. 해당 권한으로 옮겨야함.
            throw AuthExceptionExecutor.PasswordInvalid();
        }

        // 2. 중복된 회원이 있는지 조회
        if (memberRepository.existsByEmail(request.email())) {
            throw MemberExceptionExecutor.EmailOverlap();
        }

        // 3. 회원가입 DTO 값을 기반으로 Member 생성
        final Member member = memberMapper.signupRequestToEntity(request);
        memberRepository.save(member);

        // 4. 생성한 member 기반으로 dataResponse 반환
        return new MemberSignupResponse(member);
    }

    public MemberLoginResponse login(final LoginRequest request) {
        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(AuthExceptionExecutor::LoginFail);

        if (!encryptHelper.isMatch(request.password(), member.getPassword())) {
            throw AuthExceptionExecutor.LoginFail();
        }

        final String accessToken = jwtTokenProvider.createMemberToken(member.getId(), member.getName(), member.getRole());
        final String tokenType = "bearer";
        final String name = member.getName();
        final Role role = member.getRole();
        final boolean activation = member.isActivation();

        return new MemberLoginResponse(name, role, activation, accessToken, tokenType);
    }
}
