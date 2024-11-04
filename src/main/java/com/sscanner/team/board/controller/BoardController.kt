package com.sscanner.team.board.controller

import com.sscanner.team.board.requestdto.BoardCreateRequestDTO
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO
import com.sscanner.team.board.responsedto.BoardListResponseDTO
import com.sscanner.team.board.responsedto.BoardLocationInfoResponseDTO
import com.sscanner.team.board.responsedto.BoardResponseDTO
import com.sscanner.team.board.service.BoardService
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.global.common.response.ApiResponse
import com.sscanner.team.trashcan.type.TrashCategory
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/boards")
class BoardController (
    private val boardService: BoardService
) {

    @PostMapping
    fun createAddBoard(
        @Valid @RequestPart(value = "data") boardCreateRequestDTO: BoardCreateRequestDTO,
        @RequestPart(value = "files") files: List<MultipartFile>
    ): ApiResponse<BoardResponseDTO> {
        val board = boardService.createBoard(boardCreateRequestDTO, files)

        return ApiResponse.ok(201, board, "신고 게시글 저장 완료!!")
    }

    @DeleteMapping("/{boardId}")
    fun deleteBoard(@PathVariable boardId: Long): ApiResponse<Any> {
        boardService.deleteBoard(boardId)

        return ApiResponse.ok(200, "신고 게시글 삭제 완료!!")
    }

    @PatchMapping("/{boardId}")
    fun updateBoard(
        @PathVariable boardId: Long,
        @Valid @RequestPart(value = "data") boardUpdateRequestDTO: BoardUpdateRequestDTO,
        @RequestPart(value = "files", required = false) files: List<MultipartFile>
    ): ApiResponse<BoardResponseDTO> {
        val board = boardService.updateBoard(boardId, boardUpdateRequestDTO, files)

        return ApiResponse.ok(200, board, "신고 게시글 수정 완료!!")
    }

    @GetMapping
    fun readAllBoards(
        @RequestParam(value = "board_category", defaultValue = "MODIFY") boardCategory: BoardCategory,
        @RequestParam(value = "trash_category", defaultValue = "NORMAL") trashCategory: TrashCategory,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "10") size: Int
    ): ApiResponse<BoardListResponseDTO> {
        val result = boardService.getBoardList(boardCategory, trashCategory, page, size)

        return ApiResponse.ok(200, result, "신고 게시글 목록 조회 완료!!")
    }

    @GetMapping("/{boardId}")
    fun readBoard(@PathVariable boardId: Long): ApiResponse<BoardResponseDTO> {
        val boardDetailed = boardService.getBoardDetailed(boardId)

        return ApiResponse.ok(200, boardDetailed, "신고 게시글 상세 정보 조회 완료!!")
    }

    @GetMapping("/location/{boardId}")
    fun readBoardLocationInfo(@PathVariable boardId: Long): ApiResponse<BoardLocationInfoResponseDTO> {
        val boardLocationInfo = boardService.getBoardLocationInfo(boardId)

        return ApiResponse.ok(200, boardLocationInfo, "신고 게시글 위치 정보 조회 완료!!")
    }
}
