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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AdminServiceImpl(final MemberMapper memberMapper,
                            final MemberRepository memberRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    public Slice<MembersResponse> getUnapprovedMembers(final Pageable pageable) {
        final Slice<Member> members = memberRepository.findMemberByUnactivated(pageable);

        logger.info("[Result] 관리자가 승인되지 않은 회원 전체조회");
        return memberMapper.entityToReadMembersInfoResponses(members);
    }

    @Transactional
    public ApproveMemberResponse approveMember(final ApproveMemberRequest request) {

        final Long memberId = memberMapper.approveMemberRequestToMemberId(request);

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        member.activate();
        memberRepository.save(member);

        logger.info("[Result] {} 회원의 가입 승인", member.getName());
        return memberMapper.entityToApproveMemberResponses(member);
    }

}
