package com.sscanner.team.admin.controller;

import com.sscanner.team.admin.requestdto.AdminBoardRequestDTO;
import com.sscanner.team.admin.responsedto.AdminBoardListResponseDTO;
import com.sscanner.team.admin.responsedto.AdminEctBoardResponseDTO;
import com.sscanner.team.admin.responsedto.AdminModifyBoardResponseDTO;
import com.sscanner.team.admin.service.AdminService;
import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.trashcan.type.TrashCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    // 관리자 게시글 리스트 페이지
    @GetMapping("/boards")
    public ApiResponse<AdminBoardListResponseDTO> readAllBoards(
            @RequestParam(value = "approval_status", defaultValue = "REVIEWING") ApprovalStatus approvalStatus,
            @RequestParam(value = "board_category", defaultValue = "MODIFY") BoardCategory boardCategory,
            @RequestParam(value = "trash_category", defaultValue = "NORMAL") TrashCategory trashCategory,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size) {
        AdminBoardListResponseDTO boards =
                adminService.getBoards(approvalStatus, trashCategory, boardCategory, page, size);

        return ApiResponse.ok(200, boards, "관리자 게시글 목록 조회 완료!!");
    }

    @GetMapping("/boards/modify/{boardId}")
    public ApiResponse<AdminModifyBoardResponseDTO> readModifyBoardDetailed(@PathVariable Long boardId) {
        AdminModifyBoardResponseDTO modifyBoard = adminService.getModifyBoard(boardId);

        return ApiResponse.ok(200, modifyBoard, "관리자 수정 신고 게시글 상세 조회 완료!!");
    }

    @GetMapping("/boards/ect/{boardId}")
    public ApiResponse<AdminEctBoardResponseDTO> readEctBoardDetailed(@PathVariable Long boardId) {
        AdminEctBoardResponseDTO ectBoard = adminService.getEctBoard(boardId);

        return ApiResponse.ok(200, ectBoard, "어드민 등록 및 삭제 신고 게시글 상세 조회 완료!!");
    }

    @PatchMapping("/boards/{boardId}")
    public ApiResponse<?> reflectBoard(
            @PathVariable Long boardId,
            @RequestBody AdminBoardRequestDTO adminBoardRequestDTO) {
        adminService.reflectBoard(boardId, adminBoardRequestDTO);

        return ApiResponse.ok(200, "관리자 신고 게시글 반영 완료!!");
    }
}
