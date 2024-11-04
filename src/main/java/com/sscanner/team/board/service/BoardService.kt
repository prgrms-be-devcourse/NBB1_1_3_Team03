package com.sscanner.team.board.service

import com.sscanner.team.admin.responsedto.AdminBoardInfoResponseDTO
import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO
import com.sscanner.team.board.responsedto.BoardListResponseDTO
import com.sscanner.team.board.responsedto.BoardLocationInfoResponseDTO
import com.sscanner.team.board.responsedto.BoardResponseDTO
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile

interface BoardService {
    fun createBoard(boardCreateRequestDTO: BoardCreateRequestDTO, files: List<MultipartFile>): BoardResponseDTO
    fun deleteBoard(boardId: Long)
    fun updateBoard(boardId: Long, boardUpdateRequestDTO: BoardUpdateRequestDTO, files: List<MultipartFile>): BoardResponseDTO
    fun getBoardList(boardCategory: BoardCategory, trashCategory: TrashCategory, page: Int, size: Int): BoardListResponseDTO
    fun getBoardDetailed(boardId: Long): BoardResponseDTO
    fun getBoardLocationInfo(boardId: Long): BoardLocationInfoResponseDTO
    fun getBoard(boardId: Long): Board
    fun getBoardsForAdmin(approvalStatus: ApprovalStatus, trashCategory: TrashCategory, boardCategory: BoardCategory, page: Int, size: Int
    ): Page<AdminBoardInfoResponseDTO>
}
