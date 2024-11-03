package com.sscanner.team.trashcan.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.trashcan.entity.TrashcanImg;
import com.sscanner.team.trashcan.repository.TrashcanImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.sscanner.team.global.exception.ExceptionCode.BAD_FILE_EXTENSION;
import static com.sscanner.team.global.exception.ExceptionCode.INVALID_FILE_NAME;

@RequiredArgsConstructor
@Service
public class TrashcanImgService {

    @Value("${sscanner.bucket.base-url}")
    private String baseUrl;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 s3Client;

    private final TrashcanImgRepository trashcanImgRepository;



    public String uploadTrashcanImg(MultipartFile file) {

        return uploadFile(file);
    }


    public TrashcanImg saveTrashcanImg(Long trashcanId, String imgUrl) {

        TrashcanImg trashcanImg = new TrashcanImg(trashcanId, imgUrl);

        return trashcanImgRepository.save(trashcanImg);
    }


    public TrashcanImg getTrashcanImg(Long trashcanId) {

        return getTrashcanImgByTrashcanId(trashcanId);
    }

    public void deleteTrashcanImg(Long trashcanId){
        trashcanImgRepository.deleteById(trashcanId);
    }

    private TrashcanImg getTrashcanImgByTrashcanId(Long trashcanId){
        return trashcanImgRepository.findByTrashcanId(trashcanId)
                .orElseThrow(null);
    }

    // 여러개의 파일 업로드
    public String uploadFile(MultipartFile file) {

        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new IllegalArgumentException("이미지 등록 실패");
        }

        return baseUrl + "/" + fileName;
    }

    // 파일명 중복 방지 (UUID)
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new BadRequestException(INVALID_FILE_NAME);
        }

        Set<String> allowedExtensions = new HashSet<>(Arrays.asList(
                "jpg", "jpeg", "png", "heic", "HEIC"
        ));

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new BadRequestException(INVALID_FILE_NAME);
        }

        String ext = fileName.substring(lastDotIndex + 1);
        if (!allowedExtensions.contains(ext)) {
            throw new BadRequestException(BAD_FILE_EXTENSION);
        }

        return fileName.substring(lastDotIndex);
    }

}
