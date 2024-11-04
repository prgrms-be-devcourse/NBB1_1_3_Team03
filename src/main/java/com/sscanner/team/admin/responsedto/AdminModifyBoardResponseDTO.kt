package com.sscanner.team.admin.responsedto

import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.entity.BoardImg
import com.sscanner.team.trashcan.entity.Trashcan
import com.sscanner.team.trashcan.entity.TrashcanImg
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus

data class AdminModifyBoardResponseDTO(
    val trashCategory: TrashCategory,
    val trashcanImgUrl: String,
    val trashcanStatus: TrashcanStatus,
    val boardImgUrls: List<String>,
    val updatedTrashcanStatus: TrashcanStatus,
    val significant: String
) {
    companion object {
        fun of(
            trashcan: Trashcan, trashcanImg: TrashcanImg,
            board: Board, boardImgs: List<BoardImg>
        ): AdminModifyBoardResponseDTO {
            return AdminModifyBoardResponseDTO(
                trashcan.trashCategory!!,
                trashcanImg.trashcanImgUrl!!,
                trashcan.trashcanStatus!!,
                boardImgs.stream().map { boardImg: BoardImg -> boardImg.boardImgUrl }.toList(),
                board.updatedTrashcanStatus,
                board.significant
            )
        }
    }
}
