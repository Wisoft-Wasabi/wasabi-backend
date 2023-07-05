package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.CreateMemberRequest;
import io.wisoft.wasabi.domain.auth.dto.CreateMemberResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller

public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<CreateMemberResponse> signupMember(@RequestBody @Valid final CreateMemberRequest request) {
        final Long id = authService.signupMember(request);

        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new CreateMemberResponse(id));
    }

}
