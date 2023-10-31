package io.wisoft.wasabi.domain.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.wisoft.wasabi.domain.board.dto.UploadImageRequest;
import io.wisoft.wasabi.domain.board.dto.UploadImageResponse;
import io.wisoft.wasabi.domain.board.exception.BoardExceptionExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardImageServiceImpl implements BoardImageService {

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
     * TODO: 이미지 삭제 보완 필요
     */
    public void deleteImage(final String key) {

        final DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucket, key);
        amazonS3.deleteObject(deleteRequest);
    }

    private String changeImageName(final String ext) {

        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
