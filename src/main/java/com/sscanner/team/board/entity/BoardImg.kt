package com.sscanner.team.board.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "Board_img")
@SQLRestriction("deleted_at IS NULL")
class BoardImg(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_img_id", nullable = false)
    val id: Long? = null,

    @Column(name = "board_id", nullable = false)
    var boardId: Long,

    @Column(name = "board_img_url", nullable = false)
    var boardImgUrl: String
) : BaseEntity() {

    constructor() : this(
        boardId = 0L,
        boardImgUrl = ""
    )

    companion object {
        fun create(boardId: Long, boardImgUrl: String): BoardImg {
            return BoardImg(
                boardId = boardId,
                boardImgUrl = boardImgUrl
            )
        }
    }
}
