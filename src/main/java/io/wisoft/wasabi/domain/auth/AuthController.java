package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.CreateMemberRequest;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupResponseDto;
import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.global.response.dto.common.CreateMemberResponse;
import io.wisoft.wasabi.domain.auth.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.LoginResponse;
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
    public ResponseEntity<CommonResponse> signupMember(@RequestBody @Valid final CreateMemberRequest request) {
        final MemberSignupResponseDto dataResponse = authService.signupMember(request);

        CommonResponse response = CommonResponse.newInstance(dataResponse);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final LoginRequest request
    ){
        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponse(token,"bearer"));
    }



}
