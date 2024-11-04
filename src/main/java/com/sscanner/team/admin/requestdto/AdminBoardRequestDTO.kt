package com.sscanner.team.admin.requestdto

import com.sscanner.team.board.type.ApprovalStatus
import jakarta.validation.constraints.NotNull

data class AdminBoardRequestDTO(
        @NotNull(message = "사진 선택은 필수입니다.")
        val chosenImgUrl: String,

        @NotNull(message = "승인 선택은 필수입니다. 승인, 거절 중 골라주세요")
        val approvalStatus: ApprovalStatus
)
