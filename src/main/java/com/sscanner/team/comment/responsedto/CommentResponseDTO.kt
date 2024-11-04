package com.sscanner.team.comment.responsedto;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.comment.entity.Comment;

public record CommentResponseDTO(
        Long id,
        String nickname,
        String authority,
        String content
) {
    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getUser().getNickname(),
                comment.getUser().getAuthority(),
                comment.getContent()
        );
    }
}
