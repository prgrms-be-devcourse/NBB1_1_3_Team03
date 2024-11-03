package com.sscanner.team.barcode.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static com.sscanner.team.barcode.common.BarcodeConstants.*;

@Component
public class BarcodeGenerator {

    public String generateBarcodeImage(String barcodeText) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeText, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_HEIGHT);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate barcode image", e); // 구체적인 예외 처리
        }
    }
}