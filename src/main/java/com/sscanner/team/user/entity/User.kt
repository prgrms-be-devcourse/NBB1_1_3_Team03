package com.sscanner.team.user.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*
import lombok.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where

@Builder(toBuilder = true)
@Getter
@Entity
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at is NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "USER")
class User @Builder constructor(
    @field:Column(
        name = "email",
        nullable = false,
        length = 50,
        unique = true
    ) private var email: String, @field:Column(
        name = "password",
        nullable = false,
        length = 100
    ) private var password: String, @field:Column(
        name = "nickname",
        nullable = false,
        length = 10
    ) private var nickname: String, @field:Column(
        name = "phone",
        nullable = false,
        length = 15
    ) private var phone: String, @field:Column(
        name = "authority",
        nullable = false,
        length = 10
    ) private var authority: String
) :
    BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, length = 36, updatable = false)
    private var userId: String? = null


    fun changeNickname(newNickname: String) {
        this.nickname = newNickname
    }

    fun changePhone(newPhone: String) {
        this.phone = newPhone
    }

    fun changePassword(encodedPassword: String) {
        this.password = encodedPassword
    }

    fun isNicknameEqual(nickname: String): Boolean {
        return this.nickname == nickname
    }

    fun isPhoneEqual(phone: String): Boolean {
        return this.phone == phone
    }
}