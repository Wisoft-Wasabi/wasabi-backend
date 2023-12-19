package io.wisoft.wasabi.domain.admin;

import io.wisoft.wasabi.domain.admin.dto.*;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response<Slice<MembersResponse>>> getUnapprovedMembers(@PageableDefault final Pageable pageable) {
        final Slice<MembersResponse> data = adminService.getUnapprovedMembers(pageable);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.READ_MEMBER_UN_APPROVE_SUCCESS,
                        data
                )
        );
    }

    @PatchMapping("/members")
    public ResponseEntity<Response<ApproveMemberResponse>> approveMember(@RequestBody final ApproveMemberRequest request) {
        final ApproveMemberResponse data = adminService.approveMember(request);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.MEMBER_APPROVE_SUCCESS,
                        data
                )
        );
    }

    @DeleteMapping("/members")
    public ResponseEntity<Response<DeleteSignUpResponse>> deleteSignUp(@RequestBody @Valid final DeleteSignUpRequest request) {

        final DeleteSignUpResponse data = adminService.deleteSignUp(request);

        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.DELETE_SIGN_UP_SUCCESS,
                data
            )
        );
    }
}