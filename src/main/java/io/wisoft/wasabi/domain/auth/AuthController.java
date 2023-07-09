package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.MemberSignupRequestDto;
import io.wisoft.wasabi.domain.auth.dto.MemberSignupResponseDto;
import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.domain.auth.dto.MemberSigninRequestDto;
import io.wisoft.wasabi.domain.auth.dto.MemberSigninResponseDto;
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
    public ResponseEntity<CommonResponse> signupMember(@RequestBody @Valid final MemberSignupRequestDto request) {
        final MemberSignupResponseDto dataResponse = authService.signupMember(request);

        CommonResponse response = CommonResponse.newInstance(dataResponse);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<CommonResponse> login(@RequestBody @Valid final MemberSigninRequestDto request
    ){
        MemberSigninResponseDto dataResponse = authService.signin(request);
        CommonResponse response = CommonResponse.newInstance(dataResponse);
        return ResponseEntity.ok(response);
    }



}
