package com.sscanner.team.board.entity;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.board.requestdto.BoardUpdateRequestDTO;
import com.sscanner.team.board.type.ApprovalStatus;
import com.sscanner.team.board.type.BoardCategory;
import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE board SET deleted_at = NOW() WHERE board_id = ?")
public class Board extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "board_category", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Lob
    @Column(name = "significants")
    private String significant;

    @Column(name = "approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Column(name = "trashcan_id")
    private Long trashcanId;

    @Column(name = "latitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "road_name_address", nullable = false, length = 100)
    private String roadNameAddress;

    @Column(name = "detailed_address", nullable = false, length = 100)
    private String detailedAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "trash_category", nullable = false, length = 15)
    private TrashCategory trashCategory;

    @Column(name = "updated_trashcan_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrashcanStatus updatedTrashcanStatus;

    @Builder
    public Board(User user,
                 BoardCategory boardCategory,
                 String significant,
                 Long trashcanId,
                 BigDecimal latitude,
                 BigDecimal longitude,
                 String roadNameAddress,
                 String detailedAddress,
                 TrashCategory trashCategory,
                 TrashcanStatus updatedTrashcanStatus) {
        this.user = user;
        this.boardCategory = boardCategory;
        this.significant = significant;
        this.approvalStatus = ApprovalStatus.REVIEWING;
        this.trashcanId = trashcanId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadNameAddress = roadNameAddress;
        this.detailedAddress = detailedAddress;
        this.trashCategory = trashCategory;
        this.updatedTrashcanStatus = updatedTrashcanStatus;
    }

    public void updateBoardInfo(BoardUpdateRequestDTO boardUpdateRequestDTO) {
        this.significant = boardUpdateRequestDTO.significant();
        this.trashcanId = boardUpdateRequestDTO.trashcanId();
        this.latitude = boardUpdateRequestDTO.latitude();
        this.longitude = boardUpdateRequestDTO.longitude();
        this.roadNameAddress = boardUpdateRequestDTO.roadNameAddress();
        this.detailedAddress = boardUpdateRequestDTO.detailedAddress();
        this.trashCategory = boardUpdateRequestDTO.trashCategory();
        this.updatedTrashcanStatus = boardUpdateRequestDTO.updatedTrashcanStatus();
    }

    public void changeApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void changeTrashcanId(Long trashcanId) {
        this.trashcanId = trashcanId;
    }

    public Trashcan toEntityTrashcan() {
        return Trashcan.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .roadNameAddress(this.roadNameAddress)
                .detailedAddress(this.detailedAddress)
                .trashCategory(this.trashCategory)
                .build();
    }
}
