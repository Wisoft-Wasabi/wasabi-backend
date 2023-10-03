package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.global.config.common.annotation.Anyone;
import io.wisoft.wasabi.global.config.common.annotation.MemberId;
import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
public class BoardController<T> {

    private final BoardService<T> boardService;

    public BoardController(final BoardService<T> boardService) {
        this.boardService = boardService;
    }


    @PostMapping
    public ResponseEntity<Response<WriteBoardResponse>> writeBoard(@RequestBody @Valid final WriteBoardRequest request,
                                                                   @MemberId final Long memberId) throws ClassNotFoundException {

        final WriteBoardResponse data = boardService.writeBoard(request, memberId);
        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.BOARD_WRITE_SUCCESS,
                        data
                )
        );
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Response<ReadBoardResponse>> readBoard(@PathVariable final Long boardId,
                                                                 @Anyone final T accessId) {

        final ReadBoardResponse data = boardService.readBoard(boardId, accessId);
        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.BOARD_READ_SUCCESS,
                        data
                )
        );
    }

    @GetMapping
    public ResponseEntity<Response<Slice<SortBoardResponse>>> boardList(
            @RequestParam(name = "sortBy", defaultValue = "default") final String sortBy,
            @PageableDefault(size = 2) final Pageable pageable,
            @RequestParam(required = false) final String keyword) {

        final Slice<SortBoardResponse> data = boardService.getBoardList(sortBy, pageable, keyword);
        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.BOARD_SORTED_LIST_SUCCESS,
                        data
                )
        );
    }

    @GetMapping("/my-board")
    public ResponseEntity<Response<Slice<MyBoardsResponse>>> myBoards(@MemberId final Long memberId, final Pageable pageable) {
        final Slice<MyBoardsResponse> data = boardService.getMyBoards(memberId, pageable);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.MY_BOARD_LIST_SUCCESS,
                        data
                )
        );
    }

    @GetMapping("/my-like")
    public ResponseEntity<Response<Slice<MyLikeBoardsResponse>>> myLikeBoards(@MemberId final Long memberId, final Pageable pageable) {
        final Slice<MyLikeBoardsResponse> data = boardService.getMyLikeBoards(memberId, pageable);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.MY_LIKE_BOARD_LIST_SUCCESS,
                        data
                )
        );
    }

}
