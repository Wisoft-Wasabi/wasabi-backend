package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.MyLikeBoardsResponse;
import io.wisoft.wasabi.domain.board.dto.SortBoardResponse;
import io.wisoft.wasabi.domain.board.dto.WriteBoardRequest;
import io.wisoft.wasabi.domain.board.dto.WriteBoardResponse;
import io.wisoft.wasabi.global.config.common.annotation.Anyone;
import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/boards")
public class BoardController<T> {

    private final BoardService<T> boardService;

    public BoardController(final BoardService<T> boardService) {
        this.boardService = boardService;
    }


    @PostMapping
    public ResponseEntity<CommonResponse> writeBoard(@RequestBody @Valid final WriteBoardRequest request,
                                                     @MemberId final Long memberId) {

        final WriteBoardResponse response = boardService.writeBoard(request, memberId);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(response));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse> readBoard(@PathVariable final Long boardId,
                                                    @Anyone final T accessId) {

        final var response = boardService.readBoard(boardId, accessId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> sortedBoards(
            @RequestParam(name = "sortBy", defaultValue = "default") final String sortBy,
            @Valid @RequestParam(name = "page", defaultValue = "0") final int page,
            @Valid @RequestParam(name = "size", defaultValue = "2") final int size) {

        final Slice<SortBoardResponse> sortedBoards = boardService.getSortedBoards(sortBy, page, size);

        return ResponseEntity.ok(CommonResponse.newInstance(sortedBoards));
    }

    @GetMapping("/my-like")
    public ResponseEntity<CommonResponse> myLikeBoards(@MemberId final Long memberId, final Pageable pageable) {

        Slice<MyLikeBoardsResponse> response = boardService.getMyLikeBoards(memberId, pageable);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
