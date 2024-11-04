package com.sscanner.team.board.responsedto

import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import org.springframework.data.domain.Page

data class BoardListResponseDTO(
    val boardCategory: BoardCategory,
    val trashCategory: TrashCategory,
    val boardList: Page<BoardInfoResponseDTO>
) {

    companion object {
        fun from(
            boardCategory: BoardCategory,
            trashCategory: TrashCategory,
            boardList: Page<BoardInfoResponseDTO>
        ): BoardListResponseDTO {
            return BoardListResponseDTO(
                boardCategory,
                trashCategory,
                boardList
            )
        }
    }
}
