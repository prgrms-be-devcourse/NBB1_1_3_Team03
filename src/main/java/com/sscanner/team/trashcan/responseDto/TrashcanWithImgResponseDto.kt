package com.sscanner.team.trashcan.responseDto;

import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.entity.TrashcanImg;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.math.BigDecimal;

public record TrashcanWithImgResponseDto(
        Long id,
        BigDecimal latitude,
        BigDecimal longitude,
        String roadNameAddress,
        String detailedAddress,
        TrashCategory trashCategory,
        TrashcanStatus trashcanStatus,
        String trashcanImgUrl


) {

    public static TrashcanWithImgResponseDto of(Trashcan trashcan, TrashcanImg trashcanImg) {
        return new TrashcanWithImgResponseDto(
                trashcan.getId(),
                trashcan.getLatitude(),
                trashcan.getLongitude(),
                trashcan.getRoadNameAddress(),
                trashcan.getDetailedAddress(),
                trashcan.getTrashCategory(),
                trashcan.getTrashcanStatus(),
                trashcanImg.getTrashcanImgUrl()
        );
    }
}
