package com.sscanner.team.comment.responsedto

import com.sscanner.team.comment.entity.Comment

@JvmRecord
data class CommentResponseDTO(
    val id: Long?,
    val nickname: String,
    val authority: String,
    val content: String?
) {
    companion object {
        @JvmStatic
        fun from(comment: Comment): CommentResponseDTO {
            return CommentResponseDTO(
                comment.id,
                comment.user!!.nickname,
                comment.user!!.authority,
                comment.content
            )
        }
    }
}
