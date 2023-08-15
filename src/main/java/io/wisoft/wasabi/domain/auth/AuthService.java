package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.request.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberMapper;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.global.config.common.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
            final MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.encryptHelper = encryptHelper;
        this.memberMapper = memberMapper;
    }

    @Transactional
    public SignupResponse signup(final SignupRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw MemberExceptionExecutor.EmailOverlap();
        }

        final Member member = memberMapper.signUpRequestToEntity(request);

        memberRepository.save(member);

        return memberMapper.entityToMemberSignupResponse(member);
    }

    public LoginResponse login(final LoginRequest request) {
        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(AuthExceptionExecutor::LoginFail);

        if (!encryptHelper.isMatch(request.password(), member.getPassword())) {
            throw AuthExceptionExecutor.LoginFail();
        }

        final String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getName(), member.getRole(), member.isActivation());
        return memberMapper.mapToLoginResponse(member, accessToken);
    }

}
