package com.sscanner.team.admin.service

import com.sscanner.team.admin.requestdto.AdminBoardRequestDTO
import com.sscanner.team.admin.responsedto.AdminBoardListResponseDTO
import com.sscanner.team.admin.responsedto.AdminEctBoardResponseDTO
import com.sscanner.team.admin.responsedto.AdminModifyBoardResponseDTO
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory

interface AdminService {
    fun getBoards(
        approvalStatus: ApprovalStatus, trashCategory: TrashCategory,
        boardCategory: BoardCategory, page: Int, size: Int
    ): AdminBoardListResponseDTO

    fun getModifyBoard(boardId: Long): AdminModifyBoardResponseDTO
    fun getEctBoard(boardId: Long): AdminEctBoardResponseDTO
    fun reflectBoard(boardId: Long, adminBoardRequestDTO: AdminBoardRequestDTO)
}
