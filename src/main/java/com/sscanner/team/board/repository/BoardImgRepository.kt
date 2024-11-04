package com.sscanner.team.board.repository;

import com.sscanner.team.board.entity.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
    List<BoardImg> findAllByBoardId(Long boardId);

    @Modifying
    @Query("update BoardImg bi set bi.deletedAt = NOW() where bi.boardId = :boardId")
    void deleteAll(@Param("boardId") Long boardId);

    boolean existsByBoardIdAndAndBoardImgUrl(Long boardId, String imgUrl);
}
