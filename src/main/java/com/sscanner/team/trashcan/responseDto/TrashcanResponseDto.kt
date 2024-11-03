package com.sscanner.team.trashcan.responseDto

import com.sscanner.team.trashcan.entity.Trashcan
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus
import java.math.BigDecimal

@JvmRecord
data class TrashcanResponseDto(
    val id: Long?,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,
    val roadNameAddress: String?,
    val detailedAddress: String?,
    val trashCategory: TrashCategory?,
    val trashcanStatus: TrashcanStatus?
) {
    companion object {
        @JvmStatic
        fun from(trashcan: Trashcan): TrashcanResponseDto {
            return TrashcanResponseDto(
                trashcan.id,  // id
                trashcan.latitude,  // latitude
                trashcan.longitude,  // longitude
                trashcan.roadNameAddress,  // roadNameAddress
                trashcan.detailedAddress,  // detailedAddress
                trashcan.trashCategory,  // trashCategory
                trashcan.trashcanStatus // trashcanStatus
            )
        }
    }
}
