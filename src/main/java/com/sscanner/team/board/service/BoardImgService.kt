package com.sscanner.team.board.service

import com.sscanner.team.board.entity.BoardImg
import org.springframework.web.multipart.MultipartFile

interface BoardImgService {
    fun saveBoardImg(boardId: Long, files: List<MultipartFile>): List<BoardImg>
    fun deleteBoardImgs(boardId: Long)
    fun updateBoardImgs(boardId: Long, files: List<MultipartFile>): List<BoardImg>
    fun getBoardImgs(boardId: Long): List<BoardImg>
    fun checkExistImgUrl(boardId: Long, chosenImgUrl: String)
}
