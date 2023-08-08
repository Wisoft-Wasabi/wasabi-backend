package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;

public interface MemberService {

    ReadMemberInfoResponse getMemberInfo(final Long memberId);
}
