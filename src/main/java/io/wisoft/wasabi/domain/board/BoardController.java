package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.global.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }


    //TODO: prefix 지정시 /api 제외하기
    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<CommonResponse> readBoard(@PathVariable final Long boardId) {

        final var response = boardService.readBoard(boardId);

        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
