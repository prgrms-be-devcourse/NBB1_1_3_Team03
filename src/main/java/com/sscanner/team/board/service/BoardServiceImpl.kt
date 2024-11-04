package com.sscanner.team.board.service

import com.sscanner.team.admin.responsedto.AdminBoardInfoResponseDTO
import com.sscanner.team.board.entity.Board
import com.sscanner.team.board.entity.BoardImg
import com.sscanner.team.board.repository.BoardRepository
import com.sscanner.team.board.requestdto.BoardCreateRequestDTO
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO
import com.sscanner.team.board.responsedto.BoardInfoResponseDTO
import com.sscanner.team.board.responsedto.BoardListResponseDTO
import com.sscanner.team.board.responsedto.BoardLocationInfoResponseDTO
import com.sscanner.team.board.responsedto.BoardResponseDTO
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.global.utils.UserUtils
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.user.entity.User
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
@Slf4j
class BoardServiceImpl (
    private val boardRepository: BoardRepository,
    private val boardImgService: BoardImgService,
    private val userUtils: UserUtils
): BoardService {

    /**
     * 추가, 수정, 삭제 신고 게시글 등록
     * @param boardCreateRequestDTO - Body
     * @param files - 이미지 파일들
     * @return BoardCreateResponseDTO - 신고 게시글 관련 정보
     */
    @Transactional
    override fun createBoard(
        boardCreateRequestDTO: BoardCreateRequestDTO,
        files: List<MultipartFile>
    ): BoardResponseDTO {
        val user = userUtils.user
        val board = boardCreateRequestDTO.toEntityBoard(user)

        val savedBoard = boardRepository.save(board)
        val boardImgs = boardImgService.saveBoardImg(savedBoard.id!!, files)

        return BoardResponseDTO.of(savedBoard, boardImgs)
    }

    /**
     * 신고 게시글 논리 삭제
     * @param boardId - 게시글 id
     */
    @Transactional
    override fun deleteBoard(boardId: Long) {
        val user = userUtils.user
        val board = getBoard(boardId)

        isMatchAuthor(user, board)

        boardRepository.delete(board)
        boardImgService.deleteBoardImgs(boardId)
    }

    /**
     * 게시글 수정
     * @param boardId - 게시글 id
     * @param boardUpdateRequestDTO - Body
     * @param files - 변경된 이미지 파일들
     * @return BoardResponseDTO
     */
    @Transactional
    override fun updateBoard(
        boardId: Long,
        boardUpdateRequestDTO: BoardUpdateRequestDTO,
        files: List<MultipartFile>
    ): BoardResponseDTO {
        val user = userUtils.user
        val board = getBoard(boardId)

        isMatchAuthor(user, board)

        board.updateBoardInfo(boardUpdateRequestDTO)
        val boardImgs = getUpdatedImages(files, boardId)

        return BoardResponseDTO.of(board, boardImgs)
    }

    /**
     * 게시글 목록 조회
     * @param boardCategory - 게시글 유형
     * @param trashCategory - 쓰레기통 종류
     * @param page - 페이지
     * @param size - 한 페이지에 데이터 수
     * @return Page<BoardListResponseDTO>
    </BoardListResponseDTO> */
    override fun getBoardList(
        boardCategory: BoardCategory, trashCategory: TrashCategory,
        page: Int, size: Int
    ): BoardListResponseDTO {
        val pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "updatedAt")

        val boards = boardRepository.findAllByCategories(boardCategory, trashCategory, pageRequest)

        val boardInfos = boards.map { board: Board ->
            val boardImgs = boardImgService.getBoardImgs(board.id!!)
            BoardInfoResponseDTO.of(board, boardImgs[0].boardImgUrl)
        }

        return BoardListResponseDTO.from(boardCategory, trashCategory, boardInfos)
    }

    /**
     * 게시글 상세 조회
     * @param boardId - 게시글 id
     * @return BoardResponseDTO
     */
    override fun getBoardDetailed(boardId: Long): BoardResponseDTO {
        val board = getBoard(boardId)

        val boardImgs = boardImgService.getBoardImgs(boardId)

        return BoardResponseDTO.of(board, boardImgs)
    }

    /**
     * 게시글 위치 정보 조회
     * @param boardId - 게시글 id
     * @return BoardLocationInfoResponseDTO 위치 정보
     */
    override fun getBoardLocationInfo(boardId: Long): BoardLocationInfoResponseDTO {
        val board = getBoard(boardId)

        return BoardLocationInfoResponseDTO.from(board)
    }

    override fun getBoard(boardId: Long): Board {
        return boardRepository.findById(boardId)
            .orElseThrow {BadRequestException(ExceptionCode.NOT_EXIST_BOARD)}
    }

    override fun getBoardsForAdmin(
        approvalStatus: ApprovalStatus, trashCategory: TrashCategory,
        boardCategory: BoardCategory, page: Int, size: Int
    ): Page<AdminBoardInfoResponseDTO> {
        val pageRequest = PageRequest.of(page!!, size!!, Sort.Direction.DESC, "updatedAt")

        val boards =
            boardRepository.findAllByStatusAndCategories(
                approvalStatus,
                boardCategory,
                trashCategory,
                pageRequest
            )

        return boards.map { board: Board ->
            val boardImgs = boardImgService.getBoardImgs(board.id!!)
            AdminBoardInfoResponseDTO.of(board, boardImgs[0].boardImgUrl)
        }
    }

    private fun isMatchAuthor(user: User, board: Board) {
        if (board.user != user) {
            throw BadRequestException(ExceptionCode.MISMATCH_AUTHOR)
        }
    }

    private fun getUpdatedImages(files: List<MultipartFile>, boardId: Long): List<BoardImg> {
        return if (files[0].isEmpty) {
            boardImgService.getBoardImgs(boardId)
        } else {
            boardImgService!!.updateBoardImgs(boardId, files)
        }
    }
}
