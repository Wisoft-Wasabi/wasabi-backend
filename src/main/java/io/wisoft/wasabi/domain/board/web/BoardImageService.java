package io.wisoft.wasabi.domain.board.web;

import io.wisoft.wasabi.domain.board.web.dto.DeleteImageRequest;
import io.wisoft.wasabi.domain.board.web.dto.DeleteImageResponse;
import io.wisoft.wasabi.domain.board.web.dto.UploadImageRequest;
import io.wisoft.wasabi.domain.board.web.dto.UploadImageResponse;

public interface BoardImageService {

    UploadImageResponse saveImage(final UploadImageRequest request);

    DeleteImageResponse deleteImage(final DeleteImageRequest request);
}
