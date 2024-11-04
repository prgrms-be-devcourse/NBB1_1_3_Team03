package com.sscanner.team.board.responsedto

import com.sscanner.team.board.entity.Board
import com.sscanner.team.trashcan.type.TrashCategory
import java.math.BigDecimal

data class BoardLocationInfoResponseDTO(
    val id: Long?,
    val trashCategory: TrashCategory,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val roadNameAddress: String,
    val detailedAddress: String
) {

    companion object {
        fun from(board: Board): BoardLocationInfoResponseDTO {
            return BoardLocationInfoResponseDTO(
                board.id,
                board.trashCategory,
                board.latitude,
                board.longitude,
                board.roadNameAddress,
                board.detailedAddress
            )
        }
    }
}
