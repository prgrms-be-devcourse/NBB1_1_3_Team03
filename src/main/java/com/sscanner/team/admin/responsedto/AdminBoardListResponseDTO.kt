package com.sscanner.team.admin.responsedto

import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import org.springframework.data.domain.Page

data class AdminBoardListResponseDTO(
    val approvalStatus: ApprovalStatus,
    val trashCategory: TrashCategory,
    val boardCategory: BoardCategory,
    val boardList: Page<AdminBoardInfoResponseDTO>
) {
    companion object {
        fun of(
            approvalStatus: ApprovalStatus,
            trashCategory: TrashCategory,
            boardCategory: BoardCategory,
            boards: Page<AdminBoardInfoResponseDTO>
        ): AdminBoardListResponseDTO {
            return AdminBoardListResponseDTO(
                approvalStatus,
                trashCategory,
                boardCategory,
                boards
            )
        }
    }
}
