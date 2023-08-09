package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoResponse;

public interface MemberService {

    UpdateMemberInfoResponse updateMemberInfo(final Long memberId, final UpdateMemberInfoRequest request);
}
