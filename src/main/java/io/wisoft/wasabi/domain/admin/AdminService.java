package io.wisoft.wasabi.domain.admin;

import io.wisoft.wasabi.domain.admin.dto.request.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.response.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.response.MembersResponse;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberMapper;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    public AdminService(final MemberMapper memberMapper,
                        final MemberRepository memberRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    public Slice<MembersResponse> getMembers(final Pageable pageable, final Role role) {
        final Slice<Member> members = memberRepository.findMemberByUnactivated(pageable);

        return memberMapper.entityToReadMembersInfoResponses(members);
    }

    public ApproveMemberResponse approveMember(final ApproveMemberRequest request, final Role role){
        final Member member = memberRepository.findById(request.memberId())
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        memberRepository.updateActivationStatus(request.memberId(), true);

        return memberMapper.entityToApproveMemberResponses(member);
    }

}
