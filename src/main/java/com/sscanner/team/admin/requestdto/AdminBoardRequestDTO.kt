package com.sscanner.team.admin.requestdto;

import com.sscanner.team.board.type.ApprovalStatus;
import jakarta.validation.constraints.NotNull;

public record AdminBoardRequestDTO(
        @NotNull(message = "사진 선택은 필수입니다.")
        String chosenImgUrl,

        @NotNull(message = "승인 선택은 필수입니다. 승인, 거절 중 골라주세요")
        ApprovalStatus approvalStatus
) {

}
