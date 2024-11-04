package com.sscanner.team.board.service;

import com.sscanner.team.admin.responsedto.AdminBoardInfoResponseDTO;
import com.sscanner.team.user.entity.User;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardRepository;
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.responsedto.BoardInfoResponseDTO;
import com.sscanner.team.board.responsedto.BoardListResponseDTO;
import com.sscanner.team.board.responsedto.BoardLocationInfoResponseDTO;
import com.sscanner.team.board.responsedto.BoardResponseDTO;
import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.utils.UserUtils;
import com.sscanner.team.trashcan.type.TrashCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sscanner.team.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;
    private final UserUtils userUtils;

    /**
     * 추가, 수정, 삭제 신고 게시글 등록
     * @param boardCreateRequestDTO - Body
     * @param files - 이미지 파일들
     * @return BoardCreateResponseDTO - 신고 게시글 관련 정보
     */
    @Transactional
    @Override
    public BoardResponseDTO createBoard(BoardCreateRequestDTO boardCreateRequestDTO,
                                        List<MultipartFile> files) {
        User user = userUtils.getUser();

        Board board = boardCreateRequestDTO.toEntityBoard(user);

        Board savedBoard = boardRepository.save(board);

        List<BoardImg> boardImgs = boardImgService.saveBoardImg(savedBoard.getId(), files);

        return BoardResponseDTO.of(savedBoard, boardImgs);
    }

    /**
     * 신고 게시글 논리 삭제
     * @param boardId - 게시글 id
     */
    @Transactional
    @Override
    public void deleteBoard(Long boardId) {
        User user = userUtils.getUser();
        Board board = getBoard(boardId);

        isMatchAuthor(user, board);

        boardRepository.delete(board);
        boardImgService.deleteBoardImgs(boardId);
    }

    /**
     * 게시글 수정
     * @param boardId - 게시글 id
     * @param boardUpdateRequestDTO - Body
     * @param files - 변경된 이미지 파일들
     * @return BoardResponseDTO
     */
    @Transactional
    @Override
    public BoardResponseDTO updateBoard(Long boardId,
                                           BoardUpdateRequestDTO boardUpdateRequestDTO,
                                           List<MultipartFile> files) {
        User user = userUtils.getUser();
        Board board = getBoard(boardId);

        isMatchAuthor(user, board);

        board.updateBoardInfo(boardUpdateRequestDTO);

        List<BoardImg> boardImgs = getUpdatedImages(files, boardId);

        return BoardResponseDTO.of(board, boardImgs);
    }

    /**
     * 게시글 목록 조회
     * @param boardCategory - 게시글 유형
     * @param trashCategory - 쓰레기통 종류
     * @param page - 페이지
     * @param size - 한 페이지에 데이터 수
     * @return Page<BoardListResponseDTO>
     */
    @Override
    public BoardListResponseDTO getBoardList(BoardCategory boardCategory, TrashCategory trashCategory,
                                                Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "updatedAt");

        Page<Board> boards = boardRepository.findAllByCategories(boardCategory, trashCategory, pageRequest);

        Page<BoardInfoResponseDTO> boardInfos = boards.map(board -> {
            List<BoardImg> boardImgs = boardImgService.getBoardImgs(board.getId());
            return BoardInfoResponseDTO.of(board, boardImgs.get(0).getBoardImgUrl());
        });

        return BoardListResponseDTO.from(boardCategory, trashCategory, boardInfos);
    }

    /**
     * 게시글 상세 조회
     * @param boardId - 게시글 id
     * @return BoardResponseDTO
     */
    @Override
    public BoardResponseDTO getBoardDetailed(Long boardId) {
        Board board = getBoard(boardId);

        List<BoardImg> boardImgs = boardImgService.getBoardImgs(boardId);

        return BoardResponseDTO.of(board, boardImgs);
    }

    /**
     * 게시글 위치 정보 조회
     * @param boardId - 게시글 id
     * @return BoardLocationInfoResponseDTO 위치 정보
     */
    @Override
    public BoardLocationInfoResponseDTO getBoardLocationInfo(Long boardId) {
        Board board = getBoard(boardId);

        return BoardLocationInfoResponseDTO.from(board);
    }

    @Override
    public Board getBoard(Long boardId) {
        return boardRepository
                .findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_EXIST_BOARD));
    }

    @Override
    public Page<AdminBoardInfoResponseDTO> getBoardsForAdmin(ApprovalStatus approvalStatus, TrashCategory trashCategory,
                                                      BoardCategory boardCategory, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "updatedAt");

        Page<Board> boards =
                boardRepository.findAllByStatusAndCategories(approvalStatus, boardCategory, trashCategory, pageRequest);

        return boards.map(board -> {
            List<BoardImg> boardImgs = boardImgService.getBoardImgs(board.getId());
            return AdminBoardInfoResponseDTO.of(board, boardImgs.get(0).getBoardImgUrl());
        });
    }

    private void isMatchAuthor(User user, Board board) {
        if(!board.getUser().equals(user)) {
            throw new BadRequestException(MISMATCH_AUTHOR);
        }
    }

    private List<BoardImg> getUpdatedImages(List<MultipartFile> files, Long boardId) {
        if(files.get(0).isEmpty()) {
            return boardImgService.getBoardImgs(boardId);
        } else {
            return boardImgService.updateBoardImgs(boardId, files);
        }
    }
}
