package com.sscanner.team.comment.service;

import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO;
import com.sscanner.team.comment.responsedto.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO saveComment(CommentCreateRequestDTO commentCreateRequestDTO);
    void deleteComment(Long commentId);
    List<CommentResponseDTO> getComments(Long boardId);
    void deleteAll(Long boardId);
}
