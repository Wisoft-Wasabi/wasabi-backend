package io.wisoft.wasabi.domain.admin.application;

import io.wisoft.wasabi.domain.admin.web.dto.*;
import io.wisoft.wasabi.domain.admin.web.AdminService;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.application.MemberMapper;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AdminServiceImpl(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Slice<MembersResponse> getUnapprovedMembers(final Pageable pageable) {
        final Slice<Member> members = memberRepository.findMemberByUnactivated(pageable);

        logger.info("[Result] 관리자가 승인되지 않은 회원 전체조회");
        return MemberMapper.entityToReadMembersInfoResponses(members);
    }

    @Transactional
    public ApproveMemberResponse approveMember(final ApproveMemberRequest request) {

        final Long memberId = request.memberId();

        if (!memberRepository.existsById(memberId)) {
            throw MemberExceptionExecutor.MemberNotFound();
        }

        memberRepository.updateActivationStatus(memberId, true);

        logger.info("[Result] {} 회원의 가입 승인", memberId);
        return new ApproveMemberResponse(memberId);
    }

    @Override
    @Transactional
    public DeleteSignUpResponse deleteSignUp(final DeleteSignUpRequest request) {

        final int deletedCount = memberRepository.deleteAllByMemberIds(request.ids());

        return new DeleteSignUpResponse(deletedCount);
    }

}
