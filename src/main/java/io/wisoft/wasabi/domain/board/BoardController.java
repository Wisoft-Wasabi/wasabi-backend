package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.auth.exception.AuthExceptionExecutor;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.global.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }


    @PostMapping("/boards")
    public ResponseEntity<CommonResponse> writeBoard(@RequestBody @Valid final WriteBoardRequest request,
                                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String accessToken) {

        if (accessToken == null || accessToken.isBlank()) {
            throw AuthExceptionExecutor.UnAuthorized();
        }

        final WriteBoardResponse response = boardService.writeBoard(request);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<CommonResponse> readBoard(@PathVariable final Long boardId) {

        final var response = boardService.readBoard(boardId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
