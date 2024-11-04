package com.sscanner.team.comment.service

import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO
import com.sscanner.team.comment.responsedto.CommentResponseDTO

interface CommentService {
    fun saveComment(commentCreateRequestDTO: CommentCreateRequestDTO): CommentResponseDTO
    fun deleteComment(commentId: Long)
    fun getComments(boardId: Long): List<CommentResponseDTO>
    fun deleteAll(boardId: Long)
}
