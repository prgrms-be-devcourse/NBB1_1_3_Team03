package com.sscanner.team.comment.controller

import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO
import com.sscanner.team.comment.responsedto.CommentResponseDTO
import com.sscanner.team.comment.service.CommentService
import com.sscanner.team.global.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping
    fun createComment(@Valid @RequestBody commentCreateRequestDTO: CommentCreateRequestDTO): ApiResponse<CommentResponseDTO> {
        val commentInfo = commentService.saveComment(commentCreateRequestDTO)

        return ApiResponse.ok(201, commentInfo, "댓글 생성 완료!!")
    }

    @DeleteMapping("/{commmentId}")
    fun deleteComment(@PathVariable commmentId: Long): ApiResponse<Any> {
        commentService.deleteComment(commmentId)

        return ApiResponse.ok(200, null, "댓글 삭제 완료!!")
    }

    @GetMapping("/{boardId}")
    fun getComments(@PathVariable boardId: Long): ApiResponse<List<CommentResponseDTO>> {
        val comments = commentService.getComments(boardId)

        return ApiResponse.ok(200, comments, "댓글 조회 완료!!")
    }

    @DeleteMapping("/all/{boardId}")
    fun deleteAllComments(@PathVariable boardId: Long): ApiResponse<Void> {
        commentService.deleteAll(boardId)

        return ApiResponse.ok(200, null, "모든 댓글 삭제 완료!!")
    }
}
