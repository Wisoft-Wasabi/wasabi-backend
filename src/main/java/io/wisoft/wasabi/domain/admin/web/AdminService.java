package io.wisoft.wasabi.domain.admin.web;

import io.wisoft.wasabi.domain.admin.web.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminService {

    Slice<MembersResponse> getUnapprovedMembers(final Pageable pageable);

    ApproveMemberResponse approveMember(final ApproveMemberRequest request);

    DeleteSignUpResponse deleteSignUp(final DeleteSignUpRequest request);

}
