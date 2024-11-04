package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.util.List;
import java.util.stream.Collectors;

public record BoardResponseDTO(
    Long id,
    BoardCategory boardCategory,
    String significant,
    Long trashcanId,
    TrashCategory trashCategory,
    TrashcanStatus updatedTrashcanStatus,
    List<BoardImgResponseDTO> boardImgs
) {
    public static BoardResponseDTO of(Board board, List<BoardImg> boardImgs) {
        return new BoardResponseDTO(
                board.getId(),
                board.getBoardCategory(),
                board.getSignificant(),
                board.getTrashcanId(),
                board.getTrashCategory(),
                board.getUpdatedTrashcanStatus(),
                boardImgs.stream()
                        .map((BoardImg boardImg) -> BoardImgResponseDTO.from(boardImg))
                        .collect(Collectors.toList())
        );
    }
}
