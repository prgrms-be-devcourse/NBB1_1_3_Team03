package com.sscanner.team.admin.responsedto;

import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.entity.TrashcanImg;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;

import java.util.List;

public record AdminModifyBoardResponseDTO(
        TrashCategory trashCategory,
        String trashcanImgUrl,
        TrashcanStatus trashcanStatus,
        List<String> boardImgUrls,
        TrashcanStatus updatedTrashcanStatus,
        String significant
) {
    public static AdminModifyBoardResponseDTO of(Trashcan trashcan, TrashcanImg trashcanImg,
                                                   Board board, List<BoardImg> boardImgs) {
        return new AdminModifyBoardResponseDTO(
                trashcan.getTrashCategory(),
                trashcanImg.getTrashcanImgUrl(),
                trashcan.getTrashcanStatus(),
                boardImgs.stream().map(boardImg -> boardImg.getBoardImgUrl()).toList(),
                board.getUpdatedTrashcanStatus(),
                board.getSignificant()
        );
    }
}
