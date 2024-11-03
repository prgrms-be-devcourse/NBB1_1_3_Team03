package com.sscanner.team.barcode.service;

import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.responsedto.BarcodeResponseDto;

import java.util.List;

public interface BarcodeService {
    Barcode createAndSaveBarcode(String userId, Long productId);
    List<BarcodeResponseDto> findBarcodesByUserId(String userId);
}