    package io.wisoft.wasabi.domain.admin;

    import io.wisoft.wasabi.domain.admin.dto.request.ApproveMemberRequest;
    import io.wisoft.wasabi.domain.admin.dto.response.ApproveMemberResponse;
    import io.wisoft.wasabi.domain.admin.dto.response.MembersResponse;
    import io.wisoft.wasabi.domain.member.Role;
    import io.wisoft.wasabi.global.config.common.annotation.AdminRole;
    import io.wisoft.wasabi.global.config.web.response.CommonResponse;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Slice;
    import org.springframework.data.web.PageableDefault;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.rmi.server.RemoteRef;

    @RestController
    @RequestMapping("/admin")
    public class AdminController {
        private final AdminService adminService;

        public AdminController(final AdminService adminService) {
            this.adminService = adminService;
        }

        @GetMapping("/members")
        public ResponseEntity<CommonResponse> getMembers(@PageableDefault(size = 10) final Pageable pageable,
                                                         @AdminRole final Role role) {
            final Slice<MembersResponse> members = adminService.getMembers(pageable, role);

            return ResponseEntity.ok(CommonResponse.newInstance(members));

        }

        @PatchMapping("/members")
        public ResponseEntity<CommonResponse> approveMember(@RequestBody final ApproveMemberRequest request,
                                                            @AdminRole final Role role) {
            final ApproveMemberResponse member = adminService.approveMember(request, role);
            return ResponseEntity.ok(CommonResponse.newInstance(member));
        }
    }
