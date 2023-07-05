package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.CreateMemberRequest;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.wisoft.wasabi.domain.member.persistence.Member.*;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;

    @Autowired
    public AuthService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @Transactional
    public Long signupMember(final CreateMemberRequest request) {

        final Member member = createMember(request);
        memberRepository.save(member);

        return member.getId();
    }

    private Member createMember(final CreateMemberRequest request) {
        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setPhoneNumber(request.getPhoneNumber());
        member.setActivation(false);
        member.setRole(request.getRole());
        member.setCreateAt(LocalDateTime.now());
        return member;
    }

}
