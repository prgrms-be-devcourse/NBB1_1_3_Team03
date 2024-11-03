package com.sscanner.team.trashcan.requestDto

import com.sscanner.team.trashcan.type.TrashCategory
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@JvmRecord
data class UpdateTrashcanRequestDto(
    val latitude: @NotNull(message = "위도는 필수입니다.") @DecimalMin(
        value = "-90.0",
        message = "위도는 -90 이상이어야 합니다."
    ) @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.") BigDecimal?,

    val longitude: @NotNull(message = "경도는 필수입니다.") @DecimalMin(
        value = "-180.0",
        message = "경도는 -180 이상이어야 합니다."
    ) @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.") BigDecimal?,

    val roadNameAddress: @NotBlank(message = "도로명 주소는 필수입니다.") String?,

    val detailedAddress: String,

    val trashCategory: @NotNull(message = "카테고리는 필수입니다.") TrashCategory?

)
