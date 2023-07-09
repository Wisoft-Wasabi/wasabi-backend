package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.CreateMemberRequest;
import io.wisoft.wasabi.domain.auth.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupResponseDto;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import io.wisoft.wasabi.global.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.wasabi.domain.member.persistence.Member.*;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final EncryptHelper encryptHelper;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(
            MemberRepository memberRepository,
            EncryptHelper encryptHelper,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.encryptHelper = encryptHelper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public MemberSignupResponseDto signupMember(final CreateMemberRequest request) {
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        final Member member = createMember(request);
        member.setPassword(hashedPassword);
        Member saveMember = memberRepository.save(member);
        MemberSignupResponseDto dataResponseDto = new MemberSignupResponseDto(saveMember);

        return dataResponseDto;
    }

    @Transactional
    public String login(final LoginRequest request) {
        final Member member = memberRepository.findMemberByEmail(request.email())
                .orElseThrow(MemberExceptionExecutor.MemberNotFound());

        validatePassword(request, member.getPassword());

        final String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());

        return accessToken;

    }

    /**
     * 예외처리 필요
     */
    private void validatePassword(final LoginRequest request, final String member) {
        if (!encryptHelper.isMatch(request.password(), member)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
