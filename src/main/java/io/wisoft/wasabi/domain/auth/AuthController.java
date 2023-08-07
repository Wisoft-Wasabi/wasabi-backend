package io.wisoft.wasabi.domain.auth;


import io.wisoft.wasabi.domain.auth.dto.request.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.global.config.web.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody @Valid final SignupRequest request) {
        final SignupResponse dataResponse = authService.signup(request);
        final CommonResponse response = CommonResponse.newInstance(dataResponse);

        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(response));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody @Valid final LoginRequest request) {
        final LoginResponse dataResponse = authService.login(request);
        final CommonResponse response = CommonResponse.newInstance(dataResponse);
        return ResponseEntity.ok(response);
    }

}
