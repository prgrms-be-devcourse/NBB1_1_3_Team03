package com.sscanner.team.comment.entity

import com.sscanner.team.global.common.BaseEntity
import com.sscanner.team.user.entity.User
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE comment_id = ?")
class Comment : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    var id: Long? = null
        private set

    @Column(name = "board_id", nullable = false)
    var boardId: Long? = null
        private set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null
        private set

    @Lob
    @Column(name = "content", nullable = false)
    var content: String? = null
        private set

    constructor(
        boardId: Long?,
        user: User?,
        content: String?
    ) {
        this.boardId = boardId
        this.user = user
        this.content = content
    }

    protected constructor()

    class CommentBuilder internal constructor() {
        private var boardId: Long? = null
        private var user: User? = null
        private var content: String? = null
        fun boardId(boardId: Long?): CommentBuilder {
            this.boardId = boardId
            return this
        }

        fun user(user: User?): CommentBuilder {
            this.user = user
            return this
        }

        fun content(content: String?): CommentBuilder {
            this.content = content
            return this
        }

        fun build(): Comment {
            return Comment(this.boardId, this.user, this.content)
        }

        override fun toString(): String {
            return "Comment.CommentBuilder(boardId=" + this.boardId + ", user=" + this.user + ", content=" + this.content + ")"
        }
    }

    companion object {
        @JvmStatic
        fun builder(): CommentBuilder {
            return CommentBuilder()
        }
    }
}