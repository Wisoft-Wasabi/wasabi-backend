package io.wisoft.wasabi.domain.admin;


import io.wisoft.wasabi.domain.admin.dto.request.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.response.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.response.MembersResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

public interface AdminService {

    Slice<MembersResponse> getUnapprovedMembers(final Pageable pageable);

    @Transactional
    ApproveMemberResponse approveMember(final ApproveMemberRequest request);

}
