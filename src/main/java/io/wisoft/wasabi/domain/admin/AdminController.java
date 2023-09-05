package io.wisoft.wasabi.domain.admin;

import io.wisoft.wasabi.domain.admin.dto.request.ApproveMemberRequest;
import io.wisoft.wasabi.domain.admin.dto.response.ApproveMemberResponse;
import io.wisoft.wasabi.domain.admin.dto.response.MembersResponse;
import io.wisoft.wasabi.domain.member.Role;
import io.wisoft.wasabi.global.config.common.aop.UserRole;
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
    public ResponseEntity<CommonResponse> getUnapprovedMembers(@PageableDefault(size = 10) final Pageable pageable) {
        final Slice<MembersResponse> members = adminService.getUnapprovedMembers(pageable);

        return ResponseEntity.ok(CommonResponse.newInstance(members));

    }

    @PatchMapping("/members")
    @UserRole(Role.ADMIN)
    public ResponseEntity<CommonResponse> approveMember(@RequestBody final ApproveMemberRequest request) {
        final ApproveMemberResponse member = adminService.approveMember(request);
        return ResponseEntity.ok(CommonResponse.newInstance(member));
    }
}
