package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoResponse;
import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(final MemberRepository memberRepository, final MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    @Transactional
    public UpdateMemberInfoResponse updateMemberInfo(final Long memberId, final UpdateMemberInfoRequest request) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        member.update(
                request.name(),
                request.phoneNumber(),
                request.referenceUrl(),
                request.part(),
                request.organization(),
                request.motto());

        return memberMapper.entityToUpdateMemberInfoResponse(member);
    }

    public ReadMemberInfoResponse getMemberInfo(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        return memberMapper.entityToReadMemberInfoResponse(member);
    }
}
