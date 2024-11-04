package com.sscanner.team.board.responsedto

import com.sscanner.team.board.entity.Board

data class BoardInfoResponseDTO(
    val id: Long?,
    val boardFirstImgUrl: String,
    val roadNameAddress: String,
    val detailedAddress: String
) {

    companion object {
        fun of(board: Board, boardFirstImgUrl: String): BoardInfoResponseDTO {
            return BoardInfoResponseDTO(
                board.id,
                boardFirstImgUrl,
                board.roadNameAddress,
                board.detailedAddress
            )
        }
    }
}
