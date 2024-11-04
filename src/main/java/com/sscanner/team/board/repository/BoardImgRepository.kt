package com.sscanner.team.board.repository

import com.sscanner.team.board.entity.BoardImg
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BoardImgRepository : JpaRepository<BoardImg, Long> {
    fun findAllByBoardId(boardId: Long): List<BoardImg>
    fun existsByBoardIdAndAndBoardImgUrl(boardId: Long, imgUrl: String): Boolean

    @Modifying
    @Query("update BoardImg bi set bi.deletedAt = NOW() where bi.boardId = :boardId")
    fun deleteAll(@Param("boardId") boardId: Long)
}
