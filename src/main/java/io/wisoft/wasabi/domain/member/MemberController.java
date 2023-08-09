package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.domain.member.dto.UpdateMemberInfoRequest;
import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping
    public ResponseEntity<CommonResponse> updateMemberInfo(@MemberId final Long memberId,
                                                           @RequestBody @Valid final UpdateMemberInfoRequest request) {

        final var updateMemberInfoResponse = memberService.updateMemberInfo(memberId, request);
        return ResponseEntity.ok(CommonResponse.newInstance(updateMemberInfoResponse));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getMemberInfo(@MemberId final Long memberId) {

        final var readMemberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(CommonResponse.newInstance(readMemberInfoResponse));
    }
}
