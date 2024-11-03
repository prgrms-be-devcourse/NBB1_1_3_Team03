package com.sscanner.team.payment.entity

import com.sscanner.team.global.common.BaseEntity
import com.sscanner.team.products.entity.Product
import com.sscanner.team.user.entity.User
import com.sscanner.team.util.UUIDConverter
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "payment_record")
class PaymentRecord(
    @Id
    @Convert(converter = UUIDConverter::class)
    @Column(name = "payment_record_id", nullable = false, length = 16)
    val id: UUID? = null, // 읽기 전용으로 변경

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User, // 읽기 전용으로 변경

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product, // 읽기 전용으로 변경

    @Column(name = "payment", nullable = false)
    val payment: Int, // 읽기 전용으로 변경

    @Column(name = "barcode_url", nullable = false)
    val barcodeUrl: String // 읽기 전용으로 변경
) : BaseEntity() {

    constructor() : this(id = null, user = User(), product = Product(), payment = 0, barcodeUrl = "")

    companion object {
        fun create(id: UUID? = null, user: User, product: Product, payment: Int, barcodeUrl: String): PaymentRecord {
            return PaymentRecord(id = id, user = user, product = product, payment = payment, barcodeUrl = barcodeUrl)
        }
    }

    override fun toString(): String {
        return "PaymentRecord(id=$id, user=$user, product=$product, payment=$payment, barcodeUrl='$barcodeUrl')"
    }
}
