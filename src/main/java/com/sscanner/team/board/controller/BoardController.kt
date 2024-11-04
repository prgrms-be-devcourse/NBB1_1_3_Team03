package com.sscanner.team.board.controller;

import com.sscanner.team.board.requestdto.BoardCreateRequestDTO;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.responsedto.BoardListResponseDTO;
import com.sscanner.team.board.responsedto.BoardLocationInfoResponseDTO;
import com.sscanner.team.board.responsedto.BoardResponseDTO;
import com.sscanner.team.board.service.BoardService;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.trashcan.type.TrashCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ApiResponse<BoardResponseDTO> createAddBoard(@Valid @RequestPart(value = "data") BoardCreateRequestDTO boardCreateRequestDTO,
                                                        @RequestPart(value = "files") List<MultipartFile> files) {
        BoardResponseDTO board = boardService.createBoard(boardCreateRequestDTO, files);

        return ApiResponse.ok(201, board, "신고 게시글 저장 완료!!");
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<?> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);

        return ApiResponse.ok(200, "신고 게시글 삭제 완료!!");
    }

    @PatchMapping("/{boardId}")
    public ApiResponse<BoardResponseDTO> updateBoard(@PathVariable Long boardId,
                                                     @Valid @RequestPart(value = "data") BoardUpdateRequestDTO boardUpdateRequestDTO,
                                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        BoardResponseDTO board = boardService.updateBoard(boardId, boardUpdateRequestDTO, files);

        return ApiResponse.ok(200, board, "신고 게시글 수정 완료!!");
    }

    @GetMapping
    public ApiResponse<BoardListResponseDTO> readAllBoards(
            @RequestParam(value = "board_category", defaultValue = "MODIFY") BoardCategory boardCategory,
            @RequestParam(value = "trash_category", defaultValue = "NORMAL") TrashCategory trashCategory,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size){
        BoardListResponseDTO result = boardService.getBoardList(boardCategory, trashCategory, page, size);

        return ApiResponse.ok(200, result, "신고 게시글 목록 조회 완료!!");
    }

    @GetMapping("/{boardId}")
    public ApiResponse<BoardResponseDTO> readBoard(@PathVariable Long boardId) {
        BoardResponseDTO boardDetailed = boardService.getBoardDetailed(boardId);

        return ApiResponse.ok(200, boardDetailed, "신고 게시글 상세 정보 조회 완료!!");
    }

    @GetMapping("/location/{boardId}")
    public ApiResponse<BoardLocationInfoResponseDTO> readBoardLocationInfo(@PathVariable Long boardId) {
        BoardLocationInfoResponseDTO boardLocationInfo = boardService.getBoardLocationInfo(boardId);

        return ApiResponse.ok(200, boardLocationInfo, "신고 게시글 위치 정보 조회 완료!!");
    }
}
