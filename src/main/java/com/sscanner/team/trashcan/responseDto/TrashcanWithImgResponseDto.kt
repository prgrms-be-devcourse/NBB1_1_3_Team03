package com.sscanner.team.trashcan.responseDto

import com.sscanner.team.trashcan.entity.Trashcan
import com.sscanner.team.trashcan.entity.TrashcanImg
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus
import java.math.BigDecimal

@JvmRecord
data class TrashcanWithImgResponseDto(
    val id: Long?,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,
    val roadNameAddress: String?,
    val detailedAddress: String?,
    val trashCategory: TrashCategory?,
    val trashcanStatus: TrashcanStatus?,
    val trashcanImgUrl: String?


) {
    companion object {
        @JvmStatic
        fun of(trashcan: Trashcan?, trashcanImg: TrashcanImg): TrashcanWithImgResponseDto {
            return TrashcanWithImgResponseDto(
                trashcan!!.id,
                trashcan.latitude,
                trashcan.longitude,
                trashcan.roadNameAddress,
                trashcan.detailedAddress,
                trashcan.trashCategory,
                trashcan.trashcanStatus,
                trashcanImg.trashcanImgUrl
            )
        }
    }
}
