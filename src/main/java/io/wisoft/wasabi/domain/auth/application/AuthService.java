package io.wisoft.wasabi.domain.auth.application;

import io.wisoft.wasabi.domain.auth.web.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.web.dto.LoginResponse;
import io.wisoft.wasabi.domain.auth.web.dto.SignupRequest;
import io.wisoft.wasabi.domain.auth.web.dto.SignupResponse;
import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.application.MemberMapper;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.global.config.common.bcrypt.BcryptEncoder;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AuthService(
            final MemberRepository memberRepository,
            final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public SignupResponse signup(final SignupRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw MemberExceptionExecutor.EmailOverlap();
        }

        final Member member = MemberMapper.signUpRequestToEntity(request);

        memberRepository.save(member);

        logger.info("[Result] {} 회원의 회원가입 요청", request.name());
        return MemberMapper.entityToMemberSignupResponse(member);
    }

    public LoginResponse login(final LoginRequest request) {
        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(AuthExceptionExecutor::LoginFail);

        if (!BcryptEncoder.isMatch(request.password(), member.getPassword())) {
            throw AuthExceptionExecutor.LoginFail();
        }

        final String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole(), member.isActivation());

        logger.info("[Result] 이메일 : {} 의 로그인 요청", request.email());
        return MemberMapper.entityToLoginResponse(member, accessToken);
    }

}
