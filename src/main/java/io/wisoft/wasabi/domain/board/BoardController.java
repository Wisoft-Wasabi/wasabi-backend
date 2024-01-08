package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.*;
import io.wisoft.wasabi.global.config.web.resolver.Anyone;
import io.wisoft.wasabi.global.config.web.resolver.MemberId;
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
public class BoardController {

    private final BoardService boardService;
    private final BoardImageService boardImageService;

    public BoardController(final BoardService boardService,
                           final BoardImageService boardImageService) {
        this.boardService = boardService;
        this.boardImageService = boardImageService;
    }


    @PostMapping
    public ResponseEntity<Response<WriteBoardResponse>> writeBoard(@RequestBody @Valid final WriteBoardRequest request,
                                                                   @MemberId final Long memberId) {

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
                                                                 @Anyone final Long accessId,
                                                                 @RequestAttribute(value = "isAuthenticated") final boolean isAuthenticated) {

        final ReadBoardResponse data = boardService.readBoard(boardId, accessId, isAuthenticated);
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
            @PageableDefault(size = 6) final Pageable pageable,
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

    @PostMapping("/image")
    public ResponseEntity<Response<UploadImageResponse>> uploadImage(@ModelAttribute @Valid final UploadImageRequest request) {

        final UploadImageResponse data = boardImageService.saveImage(request);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.BOARD_IMAGE_UPLOAD_SUCCESS,
                        data
                )
        );
    }

    @DeleteMapping("/image")
    public ResponseEntity<Response<DeleteImageResponse>> deleteImage(@RequestBody final DeleteImageRequest request) {

        final DeleteImageResponse data = boardImageService.deleteImage(request);

        return ResponseEntity.ofNullable(
                Response.of(
                        ResponseType.BOARD_IMAGE_DELETE_SUCCESS,
                        data
                )
        );
    }
}
