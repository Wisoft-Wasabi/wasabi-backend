package io.wisoft.wasabi.domain.auth.web;

import io.wisoft.wasabi.domain.auth.application.AuthService;
import io.wisoft.wasabi.domain.auth.web.dto.*;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<SignupResponse>> signup(@RequestBody @Valid final SignupRequest request) {
        final SignupResponse data = authService.signup(request);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.SIGN_UP_SUCCESS,
                        data
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody @Valid final LoginRequest request) {
        final LoginResponse data = authService.login(request);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.LOGIN_SUCCESS,
                        data
                )
        );
    }

    @PostMapping("/mail")
    public ResponseEntity<Response<VerifyEmailResponse>> verifyEmail(@RequestBody @Valid final VerifyEmailRequest request) {

        final VerifyEmailResponse data = authService.verifyEmail(request);

        return ResponseEntity.ofNullable(
            Response.of(
                ResponseType.SEND_AUTH_CODE_SUCCESS,
                data
            )
        );
    }

}