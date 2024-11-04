package com.sscanner.team.comment.requestdto

import com.sscanner.team.comment.entity.Comment
import com.sscanner.team.comment.entity.Comment.Companion.builder
import com.sscanner.team.user.entity.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JvmRecord
data class CommentCreateRequestDTO(
    @JvmField val boardId: @NotNull(message = "boardId 작성은 필수입니다.") Long?,

    val content: @NotBlank(message = "내용 작성은 필수입니다.") String?
) {
    fun toEntityComment(user: User?): Comment {
        return builder()
            .boardId(boardId)
            .user(user)
            .content(content)
            .build()
    }
}
