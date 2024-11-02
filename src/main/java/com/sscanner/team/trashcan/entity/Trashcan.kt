package com.sscanner.team.trashcan.entity

import com.sscanner.team.global.common.BaseEntity
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto
import com.sscanner.team.trashcan.type.TrashCategory
import com.sscanner.team.trashcan.type.TrashcanStatus
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.math.BigDecimal


@SQLRestriction("deleted_at is NULL")
@Entity
@Table(name = "trashcan")
class Trashcan : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trashcan_id", nullable = false)
    var id: Long? = null
        private set

    @Column(name = "latitude", nullable = false, precision = 11, scale = 8)
    var latitude: BigDecimal? = null
        private set

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    var longitude: BigDecimal? = null
        private set

    @Column(name = "road_name_address", nullable = false, length = 100)
    var roadNameAddress: String? = null
        private set

    @Column(name = "detailed_address", nullable = false, length = 100)
    var detailedAddress: String? = null
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "trash_category", nullable = false, length = 15)
    var trashCategory: TrashCategory? = null
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "trashcan_status", nullable = false, length = 10)
    var trashcanStatus: TrashcanStatus? = null
        private set


    constructor(
        latitude: BigDecimal?,
        longitude: BigDecimal?,
        roadNameAddress: String?,
        detailedAddress: String?,
        trashCategory: TrashCategory?
    ) {
        this.latitude = latitude
        this.longitude = longitude
        this.roadNameAddress = roadNameAddress
        this.detailedAddress = detailedAddress
        this.trashCategory = trashCategory
        this.trashcanStatus = TrashcanStatus.EMPTY
    }

    protected constructor()

    fun updateInfo(requestDto: UpdateTrashcanRequestDto) {
        this.latitude = requestDto.latitude
        this.longitude = requestDto.longitude
        this.roadNameAddress = requestDto.roadNameAddress
        this.detailedAddress = requestDto.detailedAddress
        this.trashCategory = requestDto.trashCategory
    }

    fun changeTrashcanStatus(updatedTrashcanStatus: TrashcanStatus?) {
        this.trashcanStatus = updatedTrashcanStatus
    }

    class TrashcanBuilder internal constructor() {
        private var latitude: BigDecimal? = null
        private var longitude: BigDecimal? = null
        private var roadNameAddress: String? = null
        private var detailedAddress: String? = null
        private var trashCategory: TrashCategory? = null
        fun latitude(latitude: BigDecimal?): TrashcanBuilder {
            this.latitude = latitude
            return this
        }

        fun longitude(longitude: BigDecimal?): TrashcanBuilder {
            this.longitude = longitude
            return this
        }

        fun roadNameAddress(roadNameAddress: String?): TrashcanBuilder {
            this.roadNameAddress = roadNameAddress
            return this
        }

        fun detailedAddress(detailedAddress: String?): TrashcanBuilder {
            this.detailedAddress = detailedAddress
            return this
        }

        fun trashCategory(trashCategory: TrashCategory?): TrashcanBuilder {
            this.trashCategory = trashCategory
            return this
        }

        fun build(): Trashcan {
            return Trashcan(
                this.latitude,
                this.longitude,
                this.roadNameAddress,
                this.detailedAddress,
                this.trashCategory
            )
        }

        override fun toString(): String {
            return "Trashcan.TrashcanBuilder(latitude=" + this.latitude + ", longitude=" + this.longitude + ", roadNameAddress=" + this.roadNameAddress + ", detailedAddress=" + this.detailedAddress + ", trashCategory=" + this.trashCategory + ")"
        }
    }

    companion object {
        @JvmStatic
        fun builder(): TrashcanBuilder {
            return TrashcanBuilder()
        }
    }
}