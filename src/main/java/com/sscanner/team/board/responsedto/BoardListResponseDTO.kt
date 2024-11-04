package com.sscanner.team.board.responsedto;

import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;
import org.springframework.data.domain.Page;

public record BoardListResponseDTO(
        BoardCategory boardCategory,
        TrashCategory trashCategory,
        Page<BoardInfoResponseDTO> boardList
) {
    public static BoardListResponseDTO from(BoardCategory boardCategory,
                                            TrashCategory trashCategory,
                                            Page<BoardInfoResponseDTO> boardList) {
        return new BoardListResponseDTO(
                boardCategory,
                trashCategory,
                boardList
        );
    }
}
