package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.member.dto.ReadMemberInfoResponse;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoResponse;
import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping
    public ResponseEntity<Response<UpdateMemberInfoResponse>> updateMemberInfo(@MemberId final Long memberId,
                                                                               @RequestBody @Valid final UpdateMemberInfoRequest request) {

        final UpdateMemberInfoResponse data = memberService.updateMemberInfo(memberId, request);
        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.MEMBER_UPDATE_INFO_SUCCESS,
                data
            )
        );
    }

    @GetMapping
    public ResponseEntity<Response<ReadMemberInfoResponse>> getMemberInfo(@MemberId final Long memberId) {

        final ReadMemberInfoResponse data = memberService.getMemberInfo(memberId);
        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.READ_MEMBER_INFO_SUCCESS,
                data
            )
        );
    }
}
