package com.sscanner.team.admin.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.responsedto.BoardImgResponseDTO;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.util.List;

public record AdminEctBoardResponseDTO(
        BoardCategory boardCategory,
        TrashCategory trashCategory,
        List<BoardImgResponseDTO> images,
        TrashcanStatus trashcanStatus,
        String significant
) {
    public static AdminEctBoardResponseDTO of(Board board, List<BoardImg> boardImgs) {
        return new AdminEctBoardResponseDTO(
                board.getBoardCategory(),
                board.getTrashCategory(),
                boardImgs.stream()
                        .map(boardImg -> BoardImgResponseDTO.from(boardImg))
                        .toList(),
                board.getUpdatedTrashcanStatus(),
                board.getSignificant()
        );
    }
}
