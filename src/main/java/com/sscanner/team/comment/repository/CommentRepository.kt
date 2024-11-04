package com.sscanner.team.comment.repository

import com.sscanner.team.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository : JpaRepository<Comment?, Long?> {
    @Query("select c from Comment c where c.boardId = :boardId")
    fun findAllByBoardId(@Param("boardId") boardId: Long?): List<Comment?>?

    @Modifying
    @Query("update Comment c set c.deletedAt = NOW() where c.boardId = :boardId")
    fun deleteAllByBoardId(@Param("boardId") boardId: Long?)
}
