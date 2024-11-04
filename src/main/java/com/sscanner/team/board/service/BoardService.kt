package com.sscanner.team.board.service;

import com.sscanner.team.admin.responsedto.AdminBoardInfoResponseDTO;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.responsedto.BoardListResponseDTO;
import com.sscanner.team.board.responsedto.BoardLocationInfoResponseDTO;
import com.sscanner.team.board.responsedto.BoardResponseDTO;
import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.trashcan.type.TrashCategory;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    BoardResponseDTO createBoard(BoardCreateRequestDTO boardCreateRequestDTO,
                                 List<MultipartFile> files);
    void deleteBoard(Long boardId);
    BoardResponseDTO updateBoard(Long boardId,
                                 BoardUpdateRequestDTO boardUpdateRequestDTO,
                                 List<MultipartFile> files);
    BoardListResponseDTO getBoardList(BoardCategory boardCategory, TrashCategory trashCategory,
                                         Integer page, Integer size);
    BoardResponseDTO getBoardDetailed(Long boardId);
    BoardLocationInfoResponseDTO getBoardLocationInfo(Long boardId);
    Board getBoard(Long boardId);
    Page<AdminBoardInfoResponseDTO> getBoardsForAdmin(ApprovalStatus approvalStatus, TrashCategory trashCategory,
                                                      BoardCategory boardCategory, Integer page, Integer size);
}
