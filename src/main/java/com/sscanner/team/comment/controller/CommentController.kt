package com.sscanner.team.comment.controller;

import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO;
import com.sscanner.team.comment.responsedto.CommentResponseDTO;
import com.sscanner.team.comment.service.CommentService;
import com.sscanner.team.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponseDTO> createComment(@Valid @RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {
        CommentResponseDTO commentInfo = commentService.saveComment(commentCreateRequestDTO);

        return ApiResponse.ok(201, commentInfo, "댓글 생성 완료!!");
    }

    @DeleteMapping("/{commmentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commmentId) {
        commentService.deleteComment(commmentId);

        return ApiResponse.ok(200, null, "댓글 삭제 완료!!");
    }

    @GetMapping("/{boardId}")
    public ApiResponse<List<CommentResponseDTO>> getComments(@PathVariable Long boardId) {
        List<CommentResponseDTO> comments = commentService.getComments(boardId);

        return ApiResponse.ok(200, comments, "댓글 조회 완료!!");
    }

    @DeleteMapping("/all/{boardId}")
    public ApiResponse<Void> deleteAllComments(@PathVariable Long boardId) {
        commentService.deleteAll(boardId);

        return ApiResponse.ok(200, null, "모든 댓글 삭제 완료!!");
    }
}
