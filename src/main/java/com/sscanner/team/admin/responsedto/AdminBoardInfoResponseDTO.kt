package com.sscanner.team.admin.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.type.ApprovalStatus;

public record AdminBoardInfoResponseDTO(
        Long id,
        String boardFirstImgUrl,
        String roadNameAddress,
        String detailedAddress,
        ApprovalStatus approvalStatus
) {
    public static AdminBoardInfoResponseDTO of(Board board, String boardFirstImgUrl) {
        return new AdminBoardInfoResponseDTO(
                board.getId(),
                boardFirstImgUrl,
                board.getRoadNameAddress(),
                board.getDetailedAddress(),
                board.getApprovalStatus()
        );
    }
}
