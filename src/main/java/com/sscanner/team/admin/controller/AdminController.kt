package com.sscanner.team.admin.controller

import com.sscanner.team.admin.requestdto.AdminBoardRequestDTO
import com.sscanner.team.admin.responsedto.AdminBoardListResponseDTO
import com.sscanner.team.admin.responsedto.AdminEctBoardResponseDTO
import com.sscanner.team.admin.responsedto.AdminModifyBoardResponseDTO
import com.sscanner.team.admin.service.AdminService
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.trashcan.type.TrashCategory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController (
    private val adminService: AdminService
){

    // 관리자 게시글 리스트 페이지
    @GetMapping("/boards")
    fun readAllBoards(
        @RequestParam(value = "approval_status", defaultValue = "REVIEWING") approvalStatus: ApprovalStatus,
        @RequestParam(value = "board_category", defaultValue = "MODIFY") boardCategory: BoardCategory,
        @RequestParam(value = "trash_category", defaultValue = "NORMAL") trashCategory: TrashCategory,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "6") size: Int
    ): ApiResponse<AdminBoardListResponseDTO> {
        val boards =
            adminService.getBoards(approvalStatus, trashCategory, boardCategory, page, size)

        return ApiResponse.ok(200, boards, "관리자 게시글 목록 조회 완료!!")
    }

    @GetMapping("/boards/modify/{boardId}")
    fun readModifyBoardDetailed(@PathVariable boardId: Long): ApiResponse<AdminModifyBoardResponseDTO> {
        val modifyBoard = adminService.getModifyBoard(boardId)

        return ApiResponse.ok(200, modifyBoard, "관리자 수정 신고 게시글 상세 조회 완료!!")
    }

    @GetMapping("/boards/ect/{boardId}")
    fun readEctBoardDetailed(@PathVariable boardId: Long): ApiResponse<AdminEctBoardResponseDTO> {
        val ectBoard = adminService.getEctBoard(boardId)

        return ApiResponse.ok(200, ectBoard, "어드민 등록 및 삭제 신고 게시글 상세 조회 완료!!")
    }

    @PatchMapping("/boards/{boardId}")
    fun reflectBoard(
        @PathVariable boardId: Long,
        @RequestBody adminBoardRequestDTO: AdminBoardRequestDTO
    ): ApiResponse<Any> {
        adminService.reflectBoard(boardId, adminBoardRequestDTO)

        return ApiResponse.ok(200, "관리자 신고 게시글 반영 완료!!")
    }
}
