package com.sscanner.team.payment.repository

import com.sscanner.team.payment.entity.PaymentRecord
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<PaymentRecord, String>
