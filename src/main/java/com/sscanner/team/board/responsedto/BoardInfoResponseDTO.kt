package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;

public record BoardInfoResponseDTO(
        Long id,
        String boardFirstImgUrl,
        String roadNameAddress,
        String detailedAddress
) {
    public static BoardInfoResponseDTO of(Board board, String boardFirstImgUrl) {
        return new BoardInfoResponseDTO(
                board.getId(),
                boardFirstImgUrl,
                board.getRoadNameAddress(),
                board.getDetailedAddress()
        );
    }
}
