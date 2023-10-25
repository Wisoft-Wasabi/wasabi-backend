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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardImageServiceImpl implements BoardImageService {

    private final AmazonS3 amazonS3;
    private final BoardImageRepository boardImageRepository;
    private final BoardMapper boardMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public BoardImageServiceImpl(final AmazonS3 amazonS3,
                                 final BoardImageRepository boardImageRepository,
                                 final BoardMapper boardMapper) {
        this.amazonS3 = amazonS3;
        this.boardImageRepository = boardImageRepository;
        this.boardMapper = boardMapper;
    }

    @Override
    @Transactional
    public UploadImageResponse saveImage(final UploadImageRequest request) {

        final String originName = request.image().getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);

        final String storeImagePath = uploadImage(request.image(), ext, changedImageName);

        final BoardImage boardImage = boardMapper.uploadImageRequestToEntity(changedImageName, storeImagePath);
        boardImageRepository.save(boardImage);

        return boardMapper.entityToUploadImageResponse(boardImage);
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

        return boardMapper.entityToDeleteImageResponse(boardImage.getId());
    }

    /**
     * 주기적으로 게시글에 포함되지 않은 이미지 삭제(불필요한 이미지) -> 24시간 동안 사용되지 않았다면 불필요하다고 판단
     */
    @Scheduled(cron = "${cloud.aws.cron}")
    @Transactional
    public void deleteUnNecessaryImage() {

        final List<BoardImage> images = boardImageRepository.findAllBoardImagesByNull();

        images.stream()
                .filter(image -> Duration.between(image.getCreatedAt(), LocalDateTime.now()).toHours() >= 24)
                .forEach(image -> {
                    final DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucket, image.getFileName());
                    amazonS3.deleteObject(deleteRequest);
                    boardImageRepository.delete(image);
                });
    }

    private String changeImageName(final String ext) {

        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
