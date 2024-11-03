package com.sscanner.team.trashcan.repository;


import com.sscanner.team.trashcan.entity.TrashcanImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrashcanImgRepository extends JpaRepository<TrashcanImg, Long> {
    Optional<TrashcanImg> findByTrashcanId(Long trashcanId);
}
