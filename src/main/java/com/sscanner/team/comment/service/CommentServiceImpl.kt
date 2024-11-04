package com.sscanner.team.comment.service;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.board.service.BoardService;
import com.sscanner.team.comment.entity.Comment;
import com.sscanner.team.comment.repository.CommentRepository;
import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO;
import com.sscanner.team.comment.responsedto.CommentResponseDTO;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sscanner.team.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserUtils userUtils;
    private final BoardService boardService;

    @Override
    @Transactional
    public CommentResponseDTO saveComment(CommentCreateRequestDTO commentCreateRequestDTO) {
        boardService.getBoard(commentCreateRequestDTO.boardId());

        User user = userUtils.getUser();

        Comment comment = commentCreateRequestDTO.toEntityComment(user);

        commentRepository.save(comment);

        return CommentResponseDTO.from(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        User user = userUtils.getUser();
        Comment comment = getComment(commentId);

        isMatchAuthor(user, comment);

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponseDTO> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);

        return comments.stream()
                .map(comment -> CommentResponseDTO.from(comment))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAll(Long boardId) {
        commentRepository.deleteAllByBoardId(boardId);
    }

    private Comment getComment(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new BadRequestException(NOT_EXIST_COMMENT));
    }

    private void isMatchAuthor(User user, Comment comment) {
        if(!comment.getUser().equals(user)) {
            throw new BadRequestException(MISMATCH_AUTHOR);
        }
    }
}
