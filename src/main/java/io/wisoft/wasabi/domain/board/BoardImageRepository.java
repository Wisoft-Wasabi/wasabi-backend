package io.wisoft.wasabi.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Query("SELECT boardImage FROM BoardImage boardImage" +
            " WHERE boardImage.id in :list")
    List<BoardImage> findAllBoardImagesById(@Param("list") final List<Long> boardImageIds);
}
