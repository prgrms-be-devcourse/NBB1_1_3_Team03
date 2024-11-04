package com.sscanner.team.board.service;

import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardImgRepository;
import com.sscanner.team.global.common.service.ImageService;
import com.sscanner.team.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.sscanner.team.global.exception.ExceptionCode.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardImgServiceImpl implements BoardImgService{

    private final BoardImgRepository boardImgRepository;
    private final ImageService imageService;

    @Override
    public List<BoardImg> saveBoardImg(Long boardId, List<MultipartFile> files) {
        List<BoardImg> boardImgs = new ArrayList<>();
        for(MultipartFile file : files) {
            String boardImgUrl = imageService.makeImgUrl(file);

            BoardImg boardImg = BoardImg.makeBoardImg(boardId, boardImgUrl);

            boardImgs.add(boardImg);
        }

        return boardImgRepository.saveAll(boardImgs);
    }

    @Override
    public void deleteBoardImgs(Long boardId) {
        List<BoardImg> boardImgs = getBoardImgs(boardId);

        boardImgRepository.deleteAll(boardId);
    }

    @Override
    public List<BoardImg> updateBoardImgs(Long boardId, List<MultipartFile> files) {
        deleteBoardImgs(boardId);

        return saveBoardImg(boardId, files);
    }

    @Override
    public List<BoardImg> getBoardImgs(Long boardId) {
        List<BoardImg> boardImgs = boardImgRepository.findAllByBoardId(boardId);
        if(boardImgs.isEmpty()) {
            throw new BadRequestException(NOT_EXIST_BOARD_IMG);
        }

        return boardImgs;
    }

    public void checkExistImgUrl(Long boardId, String chosenImgUrl) {
        boolean isExist = boardImgRepository.existsByBoardIdAndAndBoardImgUrl(boardId, chosenImgUrl);
        if(!isExist) {
            throw new BadRequestException(NOT_EXIST_BOARD_IMG);
        }
    }
}
