package com.sscanner.team.trashcan.entity

import com.sscanner.team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "Trashcan_img")
class TrashcanImg : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trashcan_img_id", nullable = false)
    var id: Long? = null
        private set

    @Column(name = "trashcan_id", nullable = false)
    var trashcanId: Long? = null
        private set

    @Column(name = "trashcan_img_url", nullable = false)
    var trashcanImgUrl: String? = null
        private set

    constructor(trashcanId: Long?, trashcanImgUrl: String?) {
        this.trashcanId = trashcanId
        this.trashcanImgUrl = trashcanImgUrl
    }

    constructor()

    fun changeImgUrl(chosenImgUrl: String?) {
        this.trashcanImgUrl = chosenImgUrl
    }

    class TrashcanImgBuilder internal constructor() {
        private var trashcanId: Long? = null
        private var trashcanImgUrl: String? = null
        fun trashcanId(trashcanId: Long?): TrashcanImgBuilder {
            this.trashcanId = trashcanId
            return this
        }

        fun trashcanImgUrl(trashcanImgUrl: String?): TrashcanImgBuilder {
            this.trashcanImgUrl = trashcanImgUrl
            return this
        }

        fun build(): TrashcanImg {
            return TrashcanImg(this.trashcanId, this.trashcanImgUrl)
        }

        override fun toString(): String {
            return "TrashcanImg.TrashcanImgBuilder(trashcanId=" + this.trashcanId + ", trashcanImgUrl=" + this.trashcanImgUrl + ")"
        }
    }

    companion object {
        fun builder(): TrashcanImgBuilder {
            return TrashcanImgBuilder()
        }
    }
}