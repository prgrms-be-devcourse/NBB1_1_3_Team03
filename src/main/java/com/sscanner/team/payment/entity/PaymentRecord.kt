package com.sscanner.team.payment.entity;


import com.sscanner.team.global.common.BaseEntity;
import com.sscanner.team.user.entity.User;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.util.UUIDConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "payment_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRecord extends BaseEntity {
    @Id
    @Convert(converter = UUIDConverter.class)
    @Column(name = "payment_record_id", nullable = false, length = 16)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "payment", nullable = false)
    private Integer payment;

    @Column(name = "barcode_url", nullable = false)
    private String barcodeUrl;

    @Builder
    public PaymentRecord(UUID id, User user, Product product, Integer payment, String barcodeUrl) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.payment = payment;
        this.barcodeUrl = barcodeUrl;
    }
}
