package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoResponse;
import io.wisoft.wasabi.domain.member.exception.MemberExceptionExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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

        return MemberMapper.entityToUpdateMemberInfoResponse(member);
    }

    public ReadMemberInfoResponse getMemberInfo(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        return MemberMapper.entityToReadMemberInfoResponse(member);
    }
}
