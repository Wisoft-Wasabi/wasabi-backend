package io.wisoft.wasabi.domain.member;

import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.CommonResponse;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<CommonResponse> getMemberInfo(@MemberId final Long memberId) {

        final var readMemberInfoResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(CommonResponse.newInstance(readMemberInfoResponse));
    }
}
