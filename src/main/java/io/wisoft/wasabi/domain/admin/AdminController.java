package io.wisoft.wasabi.domain.admin;

import io.wisoft.wasabi.domain.admin.dto.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.MembersResponse;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.aop.UserRole;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/members")
    @UserRole(Role.ADMIN)
    public ResponseEntity<Response<Slice<MembersResponse>>> getUnapprovedMembers(@PageableDefault(size = 10) final Pageable pageable) {
        final Slice<MembersResponse> data = adminService.getUnapprovedMembers(pageable);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.READ_MEMBER_UN_APPROVE_SUCCESS,
                        data
                )
        );

    }

    @PatchMapping("/members")
    @UserRole(Role.ADMIN)
    public ResponseEntity<Response<ApproveMemberResponse>> approveMember(@RequestBody final ApproveMemberRequest request) {
        final ApproveMemberResponse data = adminService.approveMember(request);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.MEMBER_APPROVE_SUCCESS,
                        data
                )
        );
    }
}