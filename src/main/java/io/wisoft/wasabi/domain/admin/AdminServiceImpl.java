package io.wisoft.wasabi.domain.admin;

import io.wisoft.wasabi.domain.admin.dto.request.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.response.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.response.MembersResponse;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.domain.member.MemberMapper;
import io.wisoft.wasabi.domain.member.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    public AdminServiceImpl(final MemberMapper memberMapper,
                            final MemberRepository memberRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    public Slice<MembersResponse> getUnapprovedMembers(final Pageable pageable) {
        final Slice<Member> members = memberRepository.findMemberByUnactivated(pageable);

        return memberMapper.entityToReadMembersInfoResponses(members);
    }

    @Transactional
    public ApproveMemberResponse approveMember(final ApproveMemberRequest request) {

        Long memberId = memberMapper.approveMemberRequestToMemberId(request);

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);
        member.activate();

        return memberMapper.entityToApproveMemberResponses(member);
    }

}
