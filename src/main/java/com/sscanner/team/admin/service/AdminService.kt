package com.sscanner.team.admin.service;

import com.sscanner.team.admin.requestdto.AdminBoardRequestDTO;
import com.sscanner.team.admin.responsedto.AdminBoardListResponseDTO;
import com.sscanner.team.admin.responsedto.AdminEctBoardResponseDTO;
import com.sscanner.team.admin.responsedto.AdminModifyBoardResponseDTO;
import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;

public interface AdminService {
    AdminBoardListResponseDTO getBoards(ApprovalStatus approvalStatus, TrashCategory trashCategory,
                                        BoardCategory boardCategory, Integer page, Integer size);
    AdminModifyBoardResponseDTO getModifyBoard(Long boardId);
    AdminEctBoardResponseDTO getEctBoard(Long boardId);
    void reflectBoard(Long boardId, AdminBoardRequestDTO adminBoardRequestDTO);
}
