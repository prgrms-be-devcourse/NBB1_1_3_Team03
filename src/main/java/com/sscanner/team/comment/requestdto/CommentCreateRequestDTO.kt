package com.sscanner.team.comment.requestdto;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequestDTO(
        @NotNull(message = "boardId 작성은 필수입니다.")
        Long boardId,

        @NotBlank(message = "내용 작성은 필수입니다.")
        String content
) {
    public Comment toEntityComment(User user) {
        return Comment.builder()
                .boardId(boardId)
                .user(user)
                .content(content)
                .build();
    }
}
