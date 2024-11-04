package com.sscanner.team.comment.entity;

import com.sscanner.team.user.entity.User;
import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE comment_id = ?")
public class Comment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public Comment(Long boardId,
                   User user,
                   String content) {
        this.boardId = boardId;
        this.user = user;
        this.content = content;
    }
}