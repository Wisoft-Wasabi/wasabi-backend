package io.wisoft.wasabi.domain.admin;


import io.wisoft.wasabi.domain.admin.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.MembersResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

public interface AdminService {

    Slice<MembersResponse> getUnapprovedMembers(final Pageable pageable);

    @Transactional
    ApproveMemberResponse approveMember(final ApproveMemberRequest request);

}
