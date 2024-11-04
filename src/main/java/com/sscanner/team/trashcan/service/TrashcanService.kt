package com.sscanner.team.trashcan.service

import com.sscanner.team.global.exception.BadRequestException
import com.sscanner.team.global.exception.ExceptionCode
import com.sscanner.team.global.utils.GeoUtils
import com.sscanner.team.trashcan.entity.Trashcan
import com.sscanner.team.trashcan.repository.TrashcanRepository
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto
import com.sscanner.team.trashcan.requestDto.UpdateTrashcanRequestDto
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto
import com.sscanner.team.trashcan.responseDto.TrashcanWithImgResponseDto
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal

@RequiredArgsConstructor
@Service
class TrashcanService(
    private val trashcanRepository: TrashcanRepository,
    private val trashcanImgService: TrashcanImgService
) {

    // 쓰레기통 위도, 경도 검색 범위 설정 (미터 단위)
    private val RADIUS_FOR_SEARCH_METERS = 200.0

    @Transactional
    fun registerTrashcan(requestDto: @Valid RegisterTrashcanRequestDto?, file: MultipartFile?): TrashcanWithImgResponseDto {
        val trashcan = requestDto!!.toEntity()
        trashcanRepository.save(trashcan)

        val imgUrl = trashcanImgService.uploadTrashcanImg(file)
        val trashcanImg = trashcanImgService.saveTrashcanImg(trashcan!!.id, imgUrl)

        return TrashcanWithImgResponseDto.of(trashcan, trashcanImg)
    }

    fun getTrashcanInfo(trashcanId: Long?): TrashcanWithImgResponseDto {
        val trashcan = getTrashcanById(trashcanId)
        val trashcanImg = trashcanImgService.getTrashcanImg(trashcanId)
        return TrashcanWithImgResponseDto.of(trashcan, trashcanImg)
    }

    fun getTrashcanById(trashcanId: Long?): Trashcan {
        return trashcanRepository.findById(trashcanId!!)
            .orElseThrow { BadRequestException(ExceptionCode.NOT_EXIST_TRASHCAN_ID) }!!
    }

    fun getTrashcanByCoordinate(
        latitude: @NotNull(message = "위도는 필수입니다.") @DecimalMin(
            message = "위도는 -90 이상이어야 합니다.",
            value = "-90.0"
        ) @DecimalMax(message = "위도는 90 이하이어야 합니다.", value = "90.0") BigDecimal?, longitude: @NotNull(message = "경도는 필수입니다.") @DecimalMin(
            message = "경도는 -180 이상이어야 합니다.",
            value = "-180.0"
        ) @DecimalMax(message = "경도는 180 이하이어야 합니다.", value = "180.0") BigDecimal?
    ): List<TrashcanResponseDto> {
        val boundingBox = GeoUtils.getBoundingBox(latitude, longitude, RADIUS_FOR_SEARCH_METERS)
        val minLat = boundingBox[0]
        val maxLat = boundingBox[1]
        val minLon = boundingBox[2]
        val maxLon = boundingBox[3]

        val trashcans = getTrashcansByLatAndLon(minLat, maxLat, minLon, maxLon)
        return convertToTrashcanResponses(trashcans)
    }

    private fun getTrashcansByLatAndLon(
        minLat: BigDecimal,
        maxLat: BigDecimal,
        minLon: BigDecimal,
        maxLon: BigDecimal
    ): List<Trashcan?>? {
        return trashcanRepository.findTrashcansWithinBoundingBox(minLat, maxLat, minLon, maxLon)
            ?.orElseThrow { BadRequestException(ExceptionCode.NOT_FOUND_NEARBY_TRASHCANS) }
    }

    @Transactional
    fun deleteTrashcanInfo(trashcanId: Long?) {
        try {
            trashcanRepository.deleteById(trashcanId)
        } catch (e: IllegalArgumentException) {
            throw BadRequestException(ExceptionCode.NOT_EXIST_TRASHCAN_ID)
        }
    }

    @Transactional
    fun updateTrashcanInfo(trashcanId: Long?, requestDto: @Valid UpdateTrashcanRequestDto?): TrashcanResponseDto {
        val trashcan = getTrashcanById(trashcanId)
        trashcan.updateInfo(requestDto)
        return TrashcanResponseDto.from(trashcan)
    }

    fun saveTrashcan(trashcan: Trashcan) {
        trashcanRepository.save(trashcan)
    }

    private fun convertToTrashcanResponses(trashcans: List<Trashcan?>?): List<TrashcanResponseDto> {
        return trashcans.orEmpty()
            .filterNotNull()
            .map { TrashcanResponseDto.from(it) }
    }

}
