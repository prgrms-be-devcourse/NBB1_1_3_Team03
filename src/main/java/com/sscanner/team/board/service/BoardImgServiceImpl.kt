package com.sscanner.team.board.service

import com.sscanner.team.board.entity.BoardImg
import com.sscanner.team.board.repository.BoardImgRepository
import com.sscanner.team.global.common.service.ImageService
import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Slf4j
class BoardImgServiceImpl (
    private val boardImgRepository: BoardImgRepository,
    private val imageService: ImageService
): BoardImgService {

    override fun saveBoardImg(boardId: Long, files: List<MultipartFile>): List<BoardImg> {
        val boardImgs: MutableList<BoardImg> = ArrayList()
        for (file in files) {
            val boardImgUrl = imageService.makeImgUrl(file)
            val boardImg: BoardImg = BoardImg.create(boardId, boardImgUrl)
            boardImgs.add(boardImg)
        }
        return boardImgRepository.saveAll(boardImgs)
    }

    override fun deleteBoardImgs(boardId: Long) {
        val boardImgs = getBoardImgs(boardId)

        boardImgRepository.deleteAll(boardImgs)
    }

    override fun updateBoardImgs(boardId: Long, files: List<MultipartFile>): List<BoardImg> {
        deleteBoardImgs(boardId)

        return saveBoardImg(boardId, files)
    }

    override fun getBoardImgs(boardId: Long): List<BoardImg> {
        val boardImgs: List<BoardImg> = boardImgRepository.findAllByBoardId(boardId)
        if (boardImgs.isEmpty()) {
            throw BadRequestException(ExceptionCode.NOT_EXIST_BOARD_IMG)
        }

        return boardImgs
    }

    override fun checkExistImgUrl(boardId: Long, chosenImgUrl: String) {
        val isExist = boardImgRepository.existsByBoardIdAndAndBoardImgUrl(boardId, chosenImgUrl)
        if (!isExist) {
            throw BadRequestException(ExceptionCode.NOT_EXIST_BOARD_IMG)
        }
    }
}
