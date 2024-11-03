package com.sscanner.team.products.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    val id: Long? = null, // 불변성을 위해 val 사용

    @Column(name = "product_name", nullable = false, length = 30)
    var name: String, // 필요한 경우 가변성 허용

    @Column(name = "product_price", nullable = false)
    var price: Int // 필요한 경우 가변성 허용
) : BaseEntity() {

    // JPA에서 사용할 기본 생성자
    constructor() : this(id = null, name = "", price = 0)

    companion object {
        fun create(id: Long? = null, name: String, price: Int): Product {
            return Product(id = id, name = name, price = price)
        }
    }

    override fun toString(): String {
        return "Product(id=$id, name='$name', price=$price)"
    }
}
