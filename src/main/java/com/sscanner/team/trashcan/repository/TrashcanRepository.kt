package com.sscanner.team.trashcan.repository

import com.sscanner.team.trashcan.entity.Trashcan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
interface TrashcanRepository : JpaRepository<Trashcan?, Long?> {
    //위도, 경도로 사각형 범위를 설정해 포함되는 값을 반환해주는 쿼리
    @Query("SELECT t FROM Trashcan t WHERE t.latitude BETWEEN :minLat AND :maxLat AND t.longitude BETWEEN :minLon AND :maxLon")
    fun findTrashcansWithinBoundingBox(
        @Param("minLat") minLat: BigDecimal?,
        @Param("maxLat") maxLat: BigDecimal?,
        @Param("minLon") minLon: BigDecimal?,
        @Param("maxLon") maxLon: BigDecimal?
    ): Optional<List<Trashcan?>?>?
}
