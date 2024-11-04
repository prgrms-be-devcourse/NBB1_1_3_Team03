package com.sscanner.team.admin.service

import com.sscanner.team.admin.requestdto.AdminBoardRequestDTO
import com.sscanner.team.admin.responsedto.AdminBoardListResponseDTO
import com.sscanner.team.admin.responsedto.AdminEctBoardResponseDTO
import com.sscanner.team.admin.responsedto.AdminModifyBoardResponseDTO
import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.entity.BoardImg
import com.sscanner.team.board.service.BoardImgService
import com.sscanner.team.board.service.BoardService
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.trashcan.entity.Trashcan
import com.sscanner.team.trashcan.service.TrashcanImgService
import com.sscanner.team.trashcan.service.TrashcanService
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminServiceImpl(
    private val boardService: BoardService,
    private val boardImgService: BoardImgService,
    private val trashcanService: TrashcanService,
    private val trashcanImgService: TrashcanImgService
) : AdminService {

    override fun getBoards(
        approvalStatus: ApprovalStatus, trashCategory: TrashCategory,
        boardCategory: BoardCategory, page: Int, size: Int
    ): AdminBoardListResponseDTO {
        val pageBoards =
            boardService.getBoardsForAdmin(approvalStatus, trashCategory, boardCategory, page, size)

        return AdminBoardListResponseDTO.of(approvalStatus, trashCategory, boardCategory, pageBoards)
    }

    override fun getModifyBoard(boardId: Long): AdminModifyBoardResponseDTO {
        val board = boardService.getBoard(boardId)

        isModifyBoard(board)

        val boardImgs = boardImgService.getBoardImgs(boardId)

        val trashcan = trashcanService.getTrashcanById(board.trashcanId)
        val trashcanImg = trashcanImgService.getTrashcanImg(board.trashcanId)

        return AdminModifyBoardResponseDTO.of(trashcan, trashcanImg, board, boardImgs)
    }

    override fun getEctBoard(boardId: Long): AdminEctBoardResponseDTO {
        val board = boardService.getBoard(boardId)

        isNotModifyBoard(board)

        val boardImgs: List<BoardImg?> = boardImgService.getBoardImgs(boardId)

        return AdminEctBoardResponseDTO.of(board, boardImgs)
    }

    @Transactional
    override fun reflectBoard(boardId: Long, adminBoardRequestDTO: AdminBoardRequestDTO) {
        val board = boardService.getBoard(boardId)

        val approvalStatus = adminBoardRequestDTO.approvalStatus
        val chosenImgUrl = adminBoardRequestDTO.chosenImgUrl

        boardImgService.checkExistImgUrl(boardId, chosenImgUrl)

        if (approvalStatus == ApprovalStatus.APPROVED) {
            processBoardApproval(board, chosenImgUrl)
        }

        board.changeApprovalStatus(approvalStatus)
    }

    private fun processBoardApproval(board: Board, chosenImgUrl: String) {
        val boardCategory = board.boardCategory

        when (boardCategory) {
            BoardCategory.MODIFY -> approveModifyBoard(board.trashcanId!!, board.updatedTrashcanStatus, chosenImgUrl)
            BoardCategory.REMOVE -> approveRemoveBoard(board.trashcanId!!)
            BoardCategory.ADD -> approveAddBoard(board, chosenImgUrl)
            else -> throw BadRequestException(ExceptionCode.MISMATCH_BOARD_TYPE)
        }
    }

    private fun approveModifyBoard(
        trashcanId: Long,
        updatedTrashcanStatus: TrashcanStatus,
        chosenImgUrl: String
    ) {
        val trashcan = trashcanService.getTrashcanById(trashcanId)
        val trashcanImg = trashcanImgService.getTrashcanImg(trashcanId)

        trashcan.changeTrashcanStatus(updatedTrashcanStatus)
        trashcanImg.changeImgUrl(chosenImgUrl)
    }

    private fun approveRemoveBoard(trashcanId: Long) {
        val trashcan = trashcanService.getTrashcanById(trashcanId)

        trashcanService.deleteTrashcanInfo(trashcan.id)
        trashcanImgService.deleteTrashcanImg(trashcan.id)
    }

    private fun approveAddBoard(board: Board, chosenImgUrl: String) {
        val trashcan: Trashcan = board.toEntityTrashcan()

        trashcanService.saveTrashcan(trashcan)
        trashcanImgService.saveTrashcanImg(trashcan.id, chosenImgUrl)

        board.changeTrashcanId(trashcan.id!!)
    }

    private fun isModifyBoard(board: Board) {
        if (board.boardCategory != BoardCategory.MODIFY) {
            throw BadRequestException(ExceptionCode.MISMATCH_BOARD_TYPE)
        }
    }

    private fun isNotModifyBoard(board: Board) {
        if (board.boardCategory == BoardCategory.MODIFY) {
            throw BadRequestException(ExceptionCode.MISMATCH_BOARD_TYPE)
        }
    }
}
