package com.sscanner.team.board.responsedto

import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.entity.BoardImg
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus

data class BoardResponseDTO(
    val id: Long?,
    val boardCategory: BoardCategory,
    val significant: String,
    val trashcanId: Long,
    val trashCategory: TrashCategory,
    val updatedTrashcanStatus: TrashcanStatus,
    val boardImgs: List<BoardImgResponseDTO>
) {

    companion object {
        fun of(board: Board, boardImgs: List<BoardImg>): BoardResponseDTO {
            return BoardResponseDTO(
                board.id,
                board.boardCategory,
                board.significant,
                board.trashcanId!!,
                board.trashCategory,
                board.updatedTrashcanStatus,
                boardImgs.map {BoardImgResponseDTO.from(it)}
            )
        }
    }
}
