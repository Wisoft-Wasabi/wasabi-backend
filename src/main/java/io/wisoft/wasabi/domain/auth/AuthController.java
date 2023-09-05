package io.wisoft.wasabi.domain.auth;

import io.wisoft.wasabi.domain.auth.dto.request.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
