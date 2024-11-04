package com.sscanner.team.admin.responsedto

import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.entity.BoardImg
import com.sscanner.team.board.responsedto.BoardImgResponseDTO
import com.sscanner.team.board.responsedto.BoardImgResponseDTO.Companion.from
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus

@JvmRecord
data class AdminEctBoardResponseDTO(
    val boardCategory: BoardCategory,
    val trashCategory: TrashCategory,
    val images: List<BoardImgResponseDTO>,
    val trashcanStatus: TrashcanStatus,
    val significant: String
) {
    companion object {
        fun of(board: Board, boardImgs: List<BoardImg?>): AdminEctBoardResponseDTO {
            return AdminEctBoardResponseDTO(
                board.boardCategory,
                board.trashCategory,
                boardImgs.stream()
                    .map { boardImg: BoardImg? ->
                        from(
                            boardImg!!
                        )
                    }
                    .toList(),
                board.updatedTrashcanStatus,
                board.significant
            )
        }
    }
}
