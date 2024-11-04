package com.sscanner.team.admin.service;

import com.sscanner.team.admin.requestdto.AdminBoardRequestDTO;
import com.sscanner.team.admin.responsedto.AdminBoardInfoResponseDTO;
import com.sscanner.team.admin.responsedto.AdminBoardListResponseDTO;
import com.sscanner.team.admin.responsedto.AdminEctBoardResponseDTO;
import com.sscanner.team.admin.responsedto.AdminModifyBoardResponseDTO;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.service.BoardImgService;
import com.sscanner.team.board.service.BoardService;
import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.entity.TrashcanImg;
import com.sscanner.team.trashcan.service.TrashcanImgService;
import com.sscanner.team.trashcan.service.TrashcanService;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sscanner.team.global.exception.ExceptionCode.MISMATCH_BOARD_TYPE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final BoardService boardService;
    private final BoardImgService boardImgService;
    private final TrashcanService trashcanService;
    private final TrashcanImgService trashcanImgService;

    @Override
    public AdminBoardListResponseDTO getBoards(ApprovalStatus approvalStatus, TrashCategory trashCategory,
                                               BoardCategory boardCategory, Integer page, Integer size) {
        Page<AdminBoardInfoResponseDTO> pageBoards =
                boardService.getBoardsForAdmin(approvalStatus, trashCategory, boardCategory, page, size);

        return AdminBoardListResponseDTO.of(approvalStatus, trashCategory, boardCategory, pageBoards);
    }

    @Override
    public AdminModifyBoardResponseDTO getModifyBoard(Long boardId) {
        Board board = boardService.getBoard(boardId);

        isModifyBoard(board);

        List<BoardImg> boardImgs = boardImgService.getBoardImgs(boardId);

        Trashcan trashcan = trashcanService.getTrashcanById(board.getTrashcanId());
        TrashcanImg trashcanImg = trashcanImgService.getTrashcanImg(board.getTrashcanId());

        return AdminModifyBoardResponseDTO.of(trashcan, trashcanImg, board, boardImgs);
    }

    @Override
    public AdminEctBoardResponseDTO getEctBoard(Long boardId) {
        Board board = boardService.getBoard(boardId);

        isNotModifyBoard(board);

        List<BoardImg> boardImgs = boardImgService.getBoardImgs(boardId);

        return AdminEctBoardResponseDTO.of(board, boardImgs);
    }

    @Transactional
    @Override
    public void reflectBoard(Long boardId, AdminBoardRequestDTO adminBoardRequestDTO) {
        Board board = boardService.getBoard(boardId);

        ApprovalStatus approvalStatus = adminBoardRequestDTO.approvalStatus();
        String chosenImgUrl = adminBoardRequestDTO.chosenImgUrl();

        boardImgService.checkExistImgUrl(boardId, chosenImgUrl);

        if(approvalStatus.equals(ApprovalStatus.APPROVED)) {
            processBoardApproval(board, chosenImgUrl);
        }

        board.changeApprovalStatus(approvalStatus);
    }

    private void processBoardApproval(Board board, String chosenImgUrl) {
        BoardCategory boardCategory = board.getBoardCategory();

        switch (boardCategory) {
            case MODIFY:
                approveModifyBoard(board.getTrashcanId(), board.getUpdatedTrashcanStatus(), chosenImgUrl);
                break;
            case REMOVE:
                approveRemoveBoard(board.getTrashcanId());
                break;
            case ADD:
                approveAddBoard(board, chosenImgUrl);
                break;
            default:
                throw new BadRequestException(MISMATCH_BOARD_TYPE);
        }
    }

    private void approveModifyBoard(Long trashcanId,
                                   TrashcanStatus updatedTrashcanStatus,
                                   String chosenImgUrl) {
        Trashcan trashcan = trashcanService.getTrashcanById(trashcanId);
        TrashcanImg trashcanImg = trashcanImgService.getTrashcanImg(trashcanId);

        trashcan.changeTrashcanStatus(updatedTrashcanStatus);
        trashcanImg.changeImgUrl(chosenImgUrl);
    }

    private void approveRemoveBoard(Long trashcanId) {
        Trashcan trashcan = trashcanService.getTrashcanById(trashcanId);

        trashcanService.deleteTrashcanInfo(trashcan.getId());
        trashcanImgService.deleteTrashcanImg(trashcan.getId());
    }

    private void approveAddBoard(Board board, String chosenImgUrl) {
        Trashcan trashcan = board.toEntityTrashcan();

        trashcanService.saveTrashcan(trashcan);
        trashcanImgService.saveTrashcanImg(trashcan.getId(), chosenImgUrl);

        board.changeTrashcanId(trashcan.getId());
    }

    private void isModifyBoard(Board board) {
        if(!board.getBoardCategory().equals(BoardCategory.MODIFY)) {
            throw new BadRequestException(MISMATCH_BOARD_TYPE);
        }
    }

    private void isNotModifyBoard(Board board) {
        if(board.getBoardCategory().equals(BoardCategory.MODIFY)) {
            throw new BadRequestException(MISMATCH_BOARD_TYPE);
        }
    }
}
