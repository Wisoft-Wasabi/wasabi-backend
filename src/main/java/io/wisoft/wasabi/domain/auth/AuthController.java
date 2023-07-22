package io.wisoft.wasabi.domain.auth;


import io.wisoft.wasabi.global.response.CommonResponse;
import io.wisoft.wasabi.domain.auth.dto.request.MemberSignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.MemberSignupResponse;
import io.wisoft.wasabi.domain.auth.dto.response.MemberLoginResponse;
import io.wisoft.wasabi.domain.auth.dto.request.MemberLoginRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody @Valid final MemberSignupRequest request) {
        final MemberSignupResponse dataResponse = authService.signup(request);
        final CommonResponse response = CommonResponse.newInstance(dataResponse);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse> login(@RequestBody @Valid final MemberLoginRequest request
    ){
        final MemberLoginResponse dataResponse = authService.login(request);
        final CommonResponse response = CommonResponse.newInstance(dataResponse);
        return ResponseEntity.ok(response);
    }

}
