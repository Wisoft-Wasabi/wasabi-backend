package io.wisoft.wasabi.domain.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.wisoft.wasabi.domain.board.dto.DeleteImageRequest;
import io.wisoft.wasabi.domain.board.dto.DeleteImageResponse;
import io.wisoft.wasabi.domain.board.dto.UploadImageRequest;
import io.wisoft.wasabi.domain.board.dto.UploadImageResponse;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import io.wisoft.wasabi.global.config.common.annotation.ValidFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardImageServiceImpl implements BoardImageService {
    private final Logger logger = LoggerFactory.getLogger(BoardImageServiceImpl.class);
    private final AmazonS3 amazonS3;
    private final BoardImageRepository boardImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public BoardImageServiceImpl(final AmazonS3 amazonS3,
                                 final BoardImageRepository boardImageRepository) {
        this.amazonS3 = amazonS3;
        this.boardImageRepository = boardImageRepository;
    }

    @Override
    @Transactional
    public UploadImageResponse saveImage(final UploadImageRequest request) {

        final String originName = request.image().getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);

        final String storeImagePath = uploadImage(request.image(), ext, changedImageName);

        final BoardImage boardImage = BoardMapper.uploadImageRequestToEntity(changedImageName, storeImagePath);
        boardImageRepository.save(boardImage);

        logger.info("[Result] 저장되지 않은 게시글에 속한 {}번 이미지 저장", boardImage.getId());

        return BoardMapper.entityToUploadImageResponse(boardImage);
    }

    private String uploadImage(final MultipartFile image,
                               final String ext,
                               final String changedImageName) {

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));
        try {
            amazonS3.putObject(new PutObjectRequest(
                    bucket, changedImageName, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (final IOException e) {
            throw BoardExceptionExecutor.BoardImageUploadFail();
        }

        return amazonS3.getUrl(bucket, changedImageName).toString();
    }

    /**
     * TODO: 이미지 삭제 보완 필요 - 임시 구현
     */
    @Override
    @Transactional
    public DeleteImageResponse deleteImage(final DeleteImageRequest request) {

        final BoardImage boardImage = boardImageRepository.findBoardImageByStoreImagePath(request.storeImagePath());

        final DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucket, boardImage.getFileName());
        amazonS3.deleteObject(deleteRequest);
        boardImageRepository.delete(boardImage);

        logger.info("[Result] {}번 게시글에 대한 {}번 이미지 삭제", boardImage.getBoard(), boardImage.getId());

        return BoardMapper.entityToDeleteImageResponse(boardImage.getId());
    }

    /**
     * 주기적으로 게시글에 포함되지 않은 이미지 삭제(불필요한 이미지) -> 24시간 동안 사용되지 않았다면 불필요하다고 판단
     */
    @Scheduled(cron = "${cloud.aws.cron}")
    @Transactional
    public void deleteUnNecessaryImage() {

        final List<BoardImage> images = boardImageRepository.findAllBoardImagesByNull();
        final List<Long> imageIds = new ArrayList<>();

        images.stream()
                .filter(image -> Duration.between(image.getCreatedAt(), LocalDateTime.now()).toHours() >= 24)
                .forEach(image -> {
                    final DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucket, image.getFileName());
                    amazonS3.deleteObject(deleteRequest);
                    imageIds.add(image.getId());
                });

        boardImageRepository.deleteBoardImagesByIds(imageIds);
        logger.info("[Result] 사용되지 않는 {}번 이미지 삭제", imageIds);
    }

    private String changeImageName(final String ext) {

        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
