package com.sscanner.team.trashcan.responseDto;

import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;


import java.math.BigDecimal;

public record TrashcanResponseDto(
        Long id,
        BigDecimal latitude,
        BigDecimal longitude,
        String roadNameAddress,
        String detailedAddress,
        TrashCategory trashCategory,
        TrashcanStatus trashcanStatus
){
    public static TrashcanResponseDto from(Trashcan trashcan) {
        return new TrashcanResponseDto(
                trashcan.getId(), // id
                trashcan.getLatitude(), // latitude
                trashcan.getLongitude(), // longitude
                trashcan.getRoadNameAddress(), // roadNameAddress
                trashcan.getDetailedAddress(), // detailedAddress
                trashcan.getTrashCategory(), // trashCategory
                trashcan.getTrashcanStatus() // trashcanStatus
        );
    }

}
