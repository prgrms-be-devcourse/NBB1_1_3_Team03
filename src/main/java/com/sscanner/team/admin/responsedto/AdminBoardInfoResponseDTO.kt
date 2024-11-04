package com.sscanner.team.admin.responsedto

import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.type.ApprovalStatus

data class AdminBoardInfoResponseDTO(
    val id: Long,
    val boardFirstImgUrl: String,
    val roadNameAddress: String,
    val detailedAddress: String,
    val approvalStatus: ApprovalStatus
) {
    companion object {
        fun of(board: Board, boardFirstImgUrl: String): AdminBoardInfoResponseDTO {
            return AdminBoardInfoResponseDTO(
                board.id!!,
                boardFirstImgUrl,
                board.roadNameAddress,
                board.detailedAddress,
                board.approvalStatus
            )
        }
    }
}
