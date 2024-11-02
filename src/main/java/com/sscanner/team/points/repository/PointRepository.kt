package com.sscanner.team.points.repository;

import com.sscanner.team.points.entity.UserPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointRepository extends JpaRepository<UserPoint, Long> {
    @Query("SELECT u FROM UserPoint u JOIN FETCH u.user WHERE u.user.userId = :userId")
    Optional<UserPoint> findByUserId(String userId);

    @Query("SELECT up FROM UserPoint up JOIN FETCH up.user")
    Page<UserPoint> findAllWithUser(Pageable pageable);
}
