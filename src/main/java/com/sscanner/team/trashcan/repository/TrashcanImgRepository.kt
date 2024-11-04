package com.sscanner.team.trashcan.repository

import com.sscanner.team.trashcan.entity.TrashcanImg
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface TrashcanImgRepository : JpaRepository<TrashcanImg?, Long?> {
    fun findByTrashcanId(trashcanId: Long?): Optional<TrashcanImg?>?
}
