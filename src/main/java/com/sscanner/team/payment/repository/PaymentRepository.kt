package com.sscanner.team.payment.repository;

import com.sscanner.team.payment.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentRecord, String> {
}
