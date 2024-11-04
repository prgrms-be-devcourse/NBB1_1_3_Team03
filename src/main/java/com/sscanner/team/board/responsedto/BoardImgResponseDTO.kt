package com.sscanner.team.board.responsedto

import com.sscanner.team.board.entity.BoardImg

data class BoardImgResponseDTO(
    val boardImgUrl: String
) {
    companion object {
        fun from(boardImg: BoardImg): BoardImgResponseDTO {
            return BoardImgResponseDTO(
                boardImg.boardImgUrl
            )
        }
    }
}
