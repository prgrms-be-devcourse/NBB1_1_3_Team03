package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.BoardImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardImgService {
    List<BoardImg> saveBoardImg(Long boardId, List<MultipartFile> files);
    void deleteBoardImgs(Long boardId);
    List<BoardImg> updateBoardImgs(Long boardId, List<MultipartFile> files);
    List<BoardImg> getBoardImgs(Long boardId);
    void checkExistImgUrl(Long boardId, String chosenImgUrl);
}
