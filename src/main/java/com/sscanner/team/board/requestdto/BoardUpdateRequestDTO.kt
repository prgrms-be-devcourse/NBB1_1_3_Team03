package com.sscanner.team.board.requestdto;

import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BoardUpdateRequestDTO(
        String significant,

        Long trashcanId,

        @NotNull(message = "위도 작성은 필수입니다.")
        @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
        @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.")
        BigDecimal latitude,

        @NotNull(message = "경도 작성은 필수입니다.")
        @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
        @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.")
        BigDecimal longitude,

        @NotBlank(message = "도로명 주소 작성은 필수입니다.")
        String roadNameAddress,

        @NotBlank(message = "상세 주소 작성은 필수입니다.")
        String detailedAddress,

        @NotNull(message = "쓰레기통 유형 작성은 필수입니다.")
        TrashCategory trashCategory,

        @NotNull(message = "쓰레기통 상태 작성은 필수입니다.")
        TrashcanStatus updatedTrashcanStatus
) {
}
