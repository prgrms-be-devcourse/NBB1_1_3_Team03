package com.sscanner.team.user.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where

@Entity
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at is NULL")
@Table(name = "USER")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, length = 36, updatable = false)
    var userId: String? = null, // userId를 null로 초기화하여 자동 생성 가능하도록 함

    @Column(name = "email", nullable = false, length = 50, unique = true)
    var email: String = "",

    @Column(name = "password", nullable = false, length = 100)
    var password: String = "",

    @Column(name = "nickname", nullable = false, length = 10)
    var nickname: String = "",

    @Column(name = "phone", nullable = false, length = 15)
    var phone: String = "",

    @Column(name = "authority", nullable = false, length = 10)
    var authority: String = ""
) : BaseEntity() {

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
