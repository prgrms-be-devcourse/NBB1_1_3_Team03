package com.sscanner.team.comment.service

import com.sscanner.team.board.service.BoardService
import com.sscanner.team.comment.entity.Comment
import com.sscanner.team.comment.repository.CommentRepository
import com.sscanner.team.comment.requestdto.CommentCreateRequestDTO
import com.sscanner.team.comment.responsedto.CommentResponseDTO
import com.sscanner.team.comment.responsedto.CommentResponseDTO.Companion.from
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.global.utils.UserUtils
import com.sscanner.team.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val userUtils: UserUtils,
    private val boardService: BoardService
) : CommentService {

    @Transactional
    override fun saveComment(commentCreateRequestDTO: CommentCreateRequestDTO): CommentResponseDTO {
        boardService.getBoard(commentCreateRequestDTO.boardId!!)

        val user = userUtils.user

        val comment = commentCreateRequestDTO.toEntityComment(user)

        commentRepository.save(comment)

        return from(comment)
    }

    @Transactional
    override fun deleteComment(commentId: Long) {
        val user = userUtils.user
        val comment = getComment(commentId)

        isMatchAuthor(user, comment)

        commentRepository.delete(comment)
    }

    override fun getComments(boardId: Long): List<CommentResponseDTO> {
        val comments = commentRepository.findAllByBoardId(boardId)

        return comments!!.stream()
            .map { comment: Comment? ->
                from(
                    comment!!
                )
            }
            .collect(Collectors.toList())
    }

    @Transactional
    override fun deleteAll(boardId: Long) {
        commentRepository.deleteAllByBoardId(boardId)
    }

    private fun getComment(commentId: Long): Comment {
        return commentRepository
            .findById(commentId)
            .orElseThrow { BadRequestException(ExceptionCode.NOT_EXIST_COMMENT) }!!
    }

    private fun isMatchAuthor(user: User, comment: Comment) {
        if (comment.user != user) {
            throw BadRequestException(ExceptionCode.MISMATCH_AUTHOR)
        }
    }
}
