package com.sscanner.team.comment.repository;

import com.sscanner.team.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.boardId = :boardId")
    List<Comment> findAllByBoardId(@Param("boardId") Long boardId);

    @Modifying
    @Query("update Comment c set c.deletedAt = NOW() where c.boardId = :boardId")
    void deleteAllByBoardId(@Param("boardId") Long boardId);
}
