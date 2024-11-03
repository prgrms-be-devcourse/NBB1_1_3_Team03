package com.sscanner.team.barcode.repository

import com.sscanner.team.barcode.entity.Barcode
import org.springframework.data.jpa.repository.JpaRepository

interface BarcodeRepository : JpaRepository<Barcode, Long> {
    fun findAllByUserId(userId: String): List<Barcode>
}
