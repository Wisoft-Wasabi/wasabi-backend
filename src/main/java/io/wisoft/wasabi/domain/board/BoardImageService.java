package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.DeleteImageRequest;
import io.wisoft.wasabi.domain.board.dto.DeleteImageResponse;
import io.wisoft.wasabi.domain.board.dto.UploadImageRequest;
import io.wisoft.wasabi.domain.board.dto.UploadImageResponse;

public interface BoardImageService {

    UploadImageResponse saveImage(final UploadImageRequest request);

    DeleteImageResponse deleteImage(final DeleteImageRequest request);
}
