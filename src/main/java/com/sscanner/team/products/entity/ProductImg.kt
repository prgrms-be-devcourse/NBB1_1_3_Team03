package com.sscanner.team.products.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_img")
class ProductImg(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id", nullable = false)
    val id: Long? = null, // val로 불변성 보장

    @Column(name = "product_id", nullable = false)
    val productId: Long, // val로 불변성 보장

    @Column(name = "product_img_url", nullable = false)
    val url: String // val로 불변성 보장
) : BaseEntity() {

    // JPA가 사용할 기본 생성자
    protected constructor() : this(productId = 0, url = "")

    companion object {
        fun create(productId: Long, url: String): ProductImg {
            return ProductImg(productId = productId, url = url)
        }
    }

    override fun toString(): String {
        return "ProductImg(id=$id, productId=$productId, url=$url)"
    }
}
