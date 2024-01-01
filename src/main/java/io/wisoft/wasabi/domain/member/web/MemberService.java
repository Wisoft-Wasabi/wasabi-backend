package io.wisoft.wasabi.domain.member.web;

import io.wisoft.wasabi.domain.member.web.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.web.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.web.dto.UpdateMemberInfoResponse;

public interface MemberService {

    ReadMemberInfoResponse getMemberInfo(final Long memberId);

    UpdateMemberInfoResponse updateMemberInfo(final Long memberId, final UpdateMemberInfoRequest request);
}