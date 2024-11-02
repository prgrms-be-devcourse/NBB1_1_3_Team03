package com.sscanner.team.trashcan.entity;

import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.trashcan.type.TrashCategory;
import com.sscanner.team.trashcan.type.TrashcanStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;


@SQLDelete(sql = "UPDATE trashcan_img SET deleted_at = CURRENT_TIMESTAMP WHERE trashcan_img_id = ?")
@SQLRestriction("deleted_at is NULL")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "Trashcan_img")
public class TrashcanImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trashcan_img_id", nullable = false)
    private Long id;

    @Column(name = "trashcan_id", nullable = false)
    private Long trashcanId;

    @Column(name = "trashcan_img_url", nullable = false)
    private String trashcanImgUrl;

    @Builder
    public TrashcanImg(Long trashcanId, String trashcanImgUrl) {
        this.trashcanId = trashcanId;
        this.trashcanImgUrl = trashcanImgUrl;

    }

    public void changeImgUrl(String chosenImgUrl) {
        this.trashcanImgUrl = chosenImgUrl;
    }
}