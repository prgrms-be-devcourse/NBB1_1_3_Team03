package com.sscanner.team.barcode.repository;

import com.sscanner.team.barcode.entity.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarcodeRepository extends JpaRepository<Barcode, Long> {
    List<Barcode> findAllByUserId(String userId);
}
