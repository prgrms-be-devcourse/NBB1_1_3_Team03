package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.trashcan.type.TrashCategory;

import java.math.BigDecimal;

public record BoardLocationInfoResponseDTO(
        Long id,
        TrashCategory trashCategory,
        BigDecimal latitude,
        BigDecimal longitude,
        String roadNameAddress,
        String detailedAddress
) {
    public static BoardLocationInfoResponseDTO from(Board board) {
        return new BoardLocationInfoResponseDTO(
                board.getId(),
                board.getTrashCategory(),
                board.getLatitude(),
                board.getLongitude(),
                board.getRoadNameAddress(),
                board.getDetailedAddress()
        );
    }
}
