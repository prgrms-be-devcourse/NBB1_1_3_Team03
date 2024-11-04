package com.sscanner.team.admin.responsedto;

import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;
import org.springframework.data.domain.Page;

public record AdminBoardListResponseDTO(
        ApprovalStatus approvalStatus,
        TrashCategory trashCategory,
        BoardCategory boardCategory,
        Page<AdminBoardInfoResponseDTO> boardList
) {
    public static AdminBoardListResponseDTO of(ApprovalStatus approvalStatus,
                                               TrashCategory trashCategory,
                                               BoardCategory boardCategory,
                                               Page<AdminBoardInfoResponseDTO> boards) {
        return new AdminBoardListResponseDTO(
                approvalStatus,
                trashCategory,
                boardCategory,
                boards
        );
    }
}
