package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.MemberSigninResponseDto;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.auth.dto.MemberSigninRequestDto;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupResponseDto;
import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import io.wisoft.wasabi.global.bcrypt.EncryptHelper;
import io.wisoft.wasabi.global.jwt.JwtTokenProvider;
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
    public MemberSignupResponseDto signupMember(final MemberSignupRequestDto request) {

        // 1. signup DTO 중, password, checkPassword가 동일한지 check
        if( !request.password().equals(request.checkPassword()) ) {
            // TODO: PasswordInvalidException은 DTO Validate 과정에 문제가 발생한 것. 해당 권한으로 옮겨야함.
            throw AuthExceptionExecutor.PasswordInvalid();
        }

        // 2. 중복된 회원이 있는지 조회
        if(memberRepository.findMemberByEmail(request.email()).isPresent()) {
           throw MemberExceptionExecutor.MemberEmailOverlap();
        }

        // 3. 회원가입 DTO 값을 기반으로 Member 생성
        final Member member = createMember(request);
        memberRepository.save(member);

        // 4. 생성한 member 기반으로 dataResponse 반환
        return new MemberSignupResponseDto(member);
    }


    public MemberSigninResponseDto signin(final MemberSigninRequestDto requestDto) {

        final Member member = memberRepository.findMemberByEmail(requestDto.email())
                .orElseThrow(AuthExceptionExecutor::SigninFail);
        if(!member.checkPassword(requestDto.password())) {
            throw AuthExceptionExecutor.SigninFail();
        }

        final String accessToken = jwtTokenProvider.createMemberToken(member.getId(), member.getName(), member.getRole());
        final String tokenType = "bearer";

        return new MemberSigninResponseDto(accessToken, tokenType);

    }

}
