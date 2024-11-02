package com.sscanner.team.points.entity;

import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.user.entity.User;
import com.sscanner.team.util.UUIDConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Getter
@Entity
@Table(name = "user_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends BaseEntity {
    @Id
    @Column(name = "user_point_id", nullable = false, length = 16)
    @Convert(converter = UUIDConverter.class)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Builder
    public UserPoint(UUID id, User user, Integer point) {
        this.id = id;
        this.user = user;
        this.point = point;
    }
}
