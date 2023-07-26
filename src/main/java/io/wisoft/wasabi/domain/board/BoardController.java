package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.ReadBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.global.annotation.MemberId;
import io.wisoft.wasabi.global.response.CommonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }


    @PostMapping
    public ResponseEntity<CommonResponse> writeBoard(@RequestBody @Valid final WriteBoardRequest request,
                                                     @MemberId @Valid @NotNull final Long memberId) {

        final WriteBoardResponse response = boardService.writeBoard(request, memberId);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(response));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse> readBoard(@PathVariable final Long boardId) {

        final var response = boardService.readBoard(boardId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping("/sorted")
    public ResponseEntity<CommonResponse> getSortedBoards(@RequestParam(name = "sortBy", defaultValue = "createdAt") final String sortBy) {
        final List<ReadBoardResponse> sortedBoards = boardService.getSortedBoards(sortBy);

        return ResponseEntity.ok(CommonResponse.newInstance(sortedBoards));
    }
}
