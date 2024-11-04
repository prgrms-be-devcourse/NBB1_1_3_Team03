package com.sscanner.team.board.repository

import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BoardRepository : JpaRepository<Board, Long> {
    @Query("select b from Board b where b.boardCategory = :boardCategory and b.trashCategory = :trashCategory")
    fun findAllByCategories(
        @Param("boardCategory") boardCategory: BoardCategory,
        @Param("trashCategory") trashCategory: TrashCategory,
        pageable: Pageable
    ): Page<Board>

    @Query("select b from Board b where b.approvalStatus = :approvalStatus and b.boardCategory = :boardCategory and b.trashCategory = :trashCategory")
    fun findAllByStatusAndCategories(
        @Param("approvalStatus") approvalStatus: ApprovalStatus,
        @Param("boardCategory") boardCategory: BoardCategory,
        @Param("trashCategory") trashCategory: TrashCategory,
        pageable: Pageable
    ): Page<Board>
}
