package com.sscanner.team.global.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import lombok.Getter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
@Getter
open class BaseEntity {
    @CreatedDate
    @Column(name = "created_at")
    private var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime? = null

    @Column(name = "deleted_at")
    private var deletedAt: LocalDateTime? = null
}