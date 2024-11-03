package com.sscanner.team.points.entity

import com.sscanner.team.global.common.BaseEntity
import com.sscanner.team.user.entity.User
import com.sscanner.team.util.UUIDConverter
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_point")
class UserPoint private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_point_id", nullable = false, length = 16)
    @Convert(converter = UUIDConverter::class)
    val id: UUID = UUID.randomUUID(), // 불변성을 위해 val 사용

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "point", nullable = false)
    val point: Int
) : BaseEntity() {

    // JPA에서 사용할 기본 생성자는 protected로 유지
    protected constructor() : this(user = User(), point = 0)

    companion object {
        fun create(id: UUID = UUID.randomUUID(), user: User, point: Int): UserPoint {
            return UserPoint(id = id, user = user, point = point)
        }
    }
}
