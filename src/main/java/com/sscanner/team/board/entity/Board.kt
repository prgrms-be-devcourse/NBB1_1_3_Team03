package com.sscanner.team.board.entity

import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO
import com.sscanner.team.board.type.ApprovalStatus
import com.sscanner.team.board.type.BoardCategory
import com.sscanner.team.global.common.BaseEntity
import com.sscanner.team.trashcan.entity.Trashcan
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus
import com.sscanner.team.user.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.math.BigDecimal

@Entity
@Table(name = "board")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE board SET deleted_at = NOW() WHERE board_id = ?")
class Board (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "board_category", nullable = false, length = 10)
    var boardCategory: BoardCategory,

    @Lob
    @Column(name = "significants")
    var significant: String,

    @Column(name = "trashcan_id")
    var trashcanId: Long? = null,

    @Column(name = "latitude", nullable = false, precision = 11, scale = 8)
    var latitude: BigDecimal,

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    var longitude: BigDecimal,

    @Column(name = "road_name_address", nullable = false, length = 100)
    var roadNameAddress: String,

    @Column(name = "detailed_address", nullable = false, length = 100)
    var detailedAddress: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "trash_category", nullable = false, length = 15)
    var trashCategory: TrashCategory,

    @Enumerated(EnumType.STRING)
    @Column(name = "updated_trashcan_status", nullable = false)
    var updatedTrashcanStatus: TrashcanStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    var approvalStatus: ApprovalStatus = ApprovalStatus.REVIEWING
) : BaseEntity() {

    constructor() : this(
        boardCategory = BoardCategory.ADD, // 기본값
        significant = "", // 기본값
        trashcanId = 0L, // 기본값
        latitude = BigDecimal.ZERO, // 기본값
        longitude = BigDecimal.ZERO, // 기본값
        roadNameAddress = "", // 기본값
        detailedAddress = "", // 기본값
        trashCategory = TrashCategory.NORMAL, // 기본값
        updatedTrashcanStatus = TrashcanStatus.EMPTY // 기본값
    )

    companion object {
        fun create(
            user: User,
            boardCategory: BoardCategory,
            significant: String,
            trashcanId: Long?,
            latitude: BigDecimal,
            longitude: BigDecimal,
            roadNameAddress: String,
            detailedAddress: String,
            trashCategory: TrashCategory,
            updatedTrashcanStatus: TrashcanStatus
        ): Board {
            return Board(
                user = user,
                boardCategory = boardCategory,
                significant = significant,
                trashcanId = trashcanId,
                latitude = latitude,
                longitude = longitude,
                roadNameAddress = roadNameAddress,
                detailedAddress = detailedAddress,
                trashCategory = trashCategory,
                approvalStatus = ApprovalStatus.REVIEWING,
                updatedTrashcanStatus = updatedTrashcanStatus
            )
        }
    }

    fun updateBoardInfo(boardUpdateRequestDTO: BoardUpdateRequestDTO) {
        this.significant = boardUpdateRequestDTO.significant
        this.trashcanId = boardUpdateRequestDTO.trashcanId
        this.latitude = boardUpdateRequestDTO.latitude
        this.longitude = boardUpdateRequestDTO.longitude
        this.roadNameAddress = boardUpdateRequestDTO.roadNameAddress
        this.detailedAddress = boardUpdateRequestDTO.detailedAddress
        this.trashCategory = boardUpdateRequestDTO.trashCategory
        this.updatedTrashcanStatus = boardUpdateRequestDTO.updatedTrashcanStatus
    }

    fun changeApprovalStatus(approvalStatus: ApprovalStatus) {
        this.approvalStatus = approvalStatus
    }

    fun changeTrashcanId(trashcanId: Long) {
        this.trashcanId = trashcanId
    }

    fun toEntityTrashcan(): Trashcan {
        return Trashcan.builder()
            .latitude(this.latitude)
            .longitude(this.longitude)
            .roadNameAddress(this.roadNameAddress)
            .detailedAddress(this.detailedAddress)
            .trashCategory(this.trashCategory)
            .build()
    }
}
