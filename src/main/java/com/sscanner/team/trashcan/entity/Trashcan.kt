package com.sscanner.team.trashcan.entity;

import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@SQLDelete(sql = "UPDATE trashcan SET deleted_at = NOW() WHERE trashcan_id = ?")
@SQLRestriction("deleted_at is NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "trashcan")
public class Trashcan extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trashcan_id", nullable = false)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "trashcan_status", nullable = false, length = 10)
    private TrashcanStatus trashcanStatus;


    @Builder
    public Trashcan(BigDecimal latitude, BigDecimal longitude, String roadNameAddress, String detailedAddress, TrashCategory trashCategory) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadNameAddress = roadNameAddress;
        this.detailedAddress = detailedAddress;
        this.trashCategory = trashCategory;
        this.trashcanStatus = TrashcanStatus.EMPTY;
    }

    public void updateInfo(UpdateTrashcanRequestDto requestDto) {
        this.latitude = requestDto.latitude();
        this.longitude = requestDto.longitude();
        this.roadNameAddress = requestDto.roadNameAddress();
        this.detailedAddress = requestDto.detailedAddress();
        this.trashCategory = requestDto.trashCategory();
    }

    public void changeTrashcanStatus(TrashcanStatus updatedTrashcanStatus) {
        this.trashcanStatus = updatedTrashcanStatus;
    }
}