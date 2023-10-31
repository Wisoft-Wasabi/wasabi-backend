package io.wisoft.wasabi.domain.board;

import io.wisoft.wasabi.domain.board.dto.UploadImageRequest;
import io.wisoft.wasabi.domain.board.dto.UploadImageResponse;

public interface BoardImageService {

    UploadImageResponse saveImage(final UploadImageRequest request);
}
