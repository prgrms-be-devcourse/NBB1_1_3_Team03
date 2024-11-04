package com.sscanner.team.user.repository

import com.sscanner.team.user.entity.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.*

interface UserRepository : JpaRepository<User, String> {
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun existsByPhone(phone: String): Boolean
    fun findByEmail(email: String): Optional<User>
    fun findByPhone(phone: String): Optional<User>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user WHERE deleted_at < :date", nativeQuery = true)
    fun deleteAllByDeletedAtBefore(date: LocalDateTime) // 회원 탈퇴 30일 지난 사용자
    fun findByEmailAndPhone(email: String, phone: String): Optional<User>
}
