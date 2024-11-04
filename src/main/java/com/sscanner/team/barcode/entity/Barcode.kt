package com.sscanner.team.barcode.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "barcode")
class Barcode(
    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "barcode_url", nullable = false)
    val barcodeUrl: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    // JPA가 리플렉션을 통해 사용하기 위한 기본 생성자
    constructor() : this("", 0L, "")

    companion object {
        fun create(userId: String, productId: Long, barcodeUrl: String): Barcode {
            return Barcode(userId, productId, barcodeUrl)
        }
    }
}
