package io.wisoft.wasabi.domain.board.application;

import io.wisoft.wasabi.domain.board.persistence.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Query("SELECT boardImage FROM BoardImage boardImage WHERE boardImage.id IN :list")
    List<BoardImage> findAllBoardImagesById(@Param("list") final List<Long> boardImageIds);

    @Query("SELECT boardImage FROM BoardImage boardImage WHERE boardImage.board = NULL")
    List<BoardImage> findAllBoardImagesByNull();

    @Query("SELECT boardImage FROM BoardImage boardImage WHERE boardImage.storeImagePath = :path")
    BoardImage findBoardImageByStoreImagePath(@Param("path") final String storeImagePath);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BoardImage boardImage" +
            " WHERE boardImage.id in :list")
    void deleteBoardImagesByIds(@Param("list") final List<Long> boardImageIds);
}
