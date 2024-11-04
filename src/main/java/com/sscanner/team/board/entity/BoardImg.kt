package com.sscanner.team.board.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "Board_img")
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class BoardImg extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_img_id", nullable = false)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "board_img_url", nullable = false)
    private String boardImgUrl;

    @Builder
    public BoardImg(Long boardId, String boardImgUrl) {
        this.boardId = boardId;
        this.boardImgUrl = boardImgUrl;
    }

    public static BoardImg makeBoardImg(Long boardId, String boardImgUrl) {
        return BoardImg.builder()
                .boardId(boardId)
                .boardImgUrl(boardImgUrl)
                .build();
    }
}
