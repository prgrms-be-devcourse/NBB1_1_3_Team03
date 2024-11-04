package com.sscanner.team.board.requestdto

import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus
import com.sscanner.team.user.entity.User
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class BoardCreateRequestDTO(
    @NotNull(message = "게시판 유형 작성은 필수입니다.")
    val boardCategory: BoardCategory,

    val significant: String,

    val trashcanId: Long,

    @NotNull(message = "위도 작성은 필수입니다.")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.")
    val latitude: BigDecimal,

    @NotNull(message = "경도 작성은 필수입니다.")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.")
    val longitude:  BigDecimal,

    @NotBlank(message = "도로명 주소 작성은 필수입니다.")
    val roadNameAddress: String,

    @NotBlank(message = "상세 주소 작성은 필수입니다.")
    val detailedAddress: String,

    @NotNull(message = "쓰레기통 유형 작성은 필수입니다.")
    val trashCategory: TrashCategory,

    @NotNull(message = "쓰레기통 상태 작성은 필수입니다.")
    val updatedTrashcanStatus: TrashcanStatus
) {
    fun toEntityBoard(user: User): Board {
        return Board.create(
            user,
            this.boardCategory,
            this.significant,
            this.trashcanId,
            this.latitude,
            this.longitude,
            this.roadNameAddress,
            this.detailedAddress,
            this.trashCategory,
            this.updatedTrashcanStatus
        )
    }
}
